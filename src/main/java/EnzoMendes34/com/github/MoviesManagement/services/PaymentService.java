package EnzoMendes34.com.github.MoviesManagement.services;

import EnzoMendes34.com.github.MoviesManagement.controllers.PaymentController;
import EnzoMendes34.com.github.MoviesManagement.data.dto.PaymentIntentDTO;
import EnzoMendes34.com.github.MoviesManagement.data.dto.PaymentResponseDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.BusinessException;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ResourceNotFoundException;
import EnzoMendes34.com.github.MoviesManagement.models.Payment;
import EnzoMendes34.com.github.MoviesManagement.models.Reservation;
import EnzoMendes34.com.github.MoviesManagement.repositories.PaymentRepository;
import EnzoMendes34.com.github.MoviesManagement.repositories.ReservationRepository;
import EnzoMendes34.com.github.MoviesManagement.repositories.ReservationSeatRepository;
import EnzoMendes34.com.github.MoviesManagement.types.PaymentStatus;
import EnzoMendes34.com.github.MoviesManagement.types.ReservationStatus;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PaymentService {
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final EmailService emailService;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    public PaymentService(ReservationRepository reservationRepository,
                          PaymentRepository paymentRepository,
                          ReservationSeatRepository reservationSeatRepository,
                          EmailService emailService) {
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
        this.reservationSeatRepository = reservationSeatRepository;
        this.emailService = emailService;
    }

    //CreatePaymentIntent - Chamado quando o usuário clica em pagar - cria um paymentIntent no stripe e salva o pagamento como PENDING
    public PaymentIntentDTO createPaymentIntent(Long reservationId, Long userId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found."));

        if(!reservation.getUser().getId().equals(userId)){
            throw new BusinessException("Access denied.");
        }

        if(reservation.getStatus() != ReservationStatus.PENDING){
            throw new BusinessException("Reservation is not pending. Try again");
        }

        long amountInCents = reservation.getTotalPriceInCents();

        try{
            Stripe.apiKey = stripeSecretKey;

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("brl")
                    .putMetadata("reservationId", reservationId.toString())
                    .putMetadata("userId", userId.toString())
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            //Salva o pagamento como pending no banco;
            Payment payment = new Payment();
            payment.setReservation(reservation);
            payment.setStripePaymentIntentId(intent.getId());
            payment.setAmountInCents(reservation.getTotalPriceInCents());
            payment.setCurrency("BRL");
            payment.setStatus(PaymentStatus.PENDING);
            paymentRepository.save(payment);

            return new PaymentIntentDTO(intent.getClientSecret());

        } catch (StripeException e){
            throw new BusinessException("Error creating payment intent: " + e.getMessage());
        }
    }

    //Esse método será chamado pelo front para verificar o status de pagamento após o redirect do Stripe

    public PaymentResponseDTO getPaymentStatus(Long reservationId){
        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found."));

        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setReservationId(reservationId);
        dto.setAmountInCents(payment.getAmountInCents());
        dto.setCurrency(payment.getCurrency());
        dto.setStatus(payment.getStatus());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setPaidAt(payment.getPaidAt());

        return dto.add(linkTo(methodOn(PaymentController.class).getPaymentStatus(dto.getReservationId())).withSelfRel().withType("GET"));
    }

    //handle webhook = chamado pelo stripe após o pagamento ser processado, o Stripe que cuida da segurança não o JWT
    public void handleWebhook(String payload, String sigHeader){
        Event event;

        try{
            //Valida a assinatura e se for inválida lança a exceção
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            throw new BusinessException("Invalid Stripe webhook signature");
        }

        //Roteia o evento para o handler correto
        switch (event.getType()) {
            case "payment_intent.succeeded" -> handlePaymentSucceeded(event);
            case "payment_intent.payment_failed" -> handlePaymentFailed(event);
            default -> {} //ignora os eventos não tratados
        }
    }

    //handlePaymentSucceeded - chama quando o stripe confirma o pagamento, coloca o pagamento como succeeded, confirma a reservation e manda o e-mail de confirmação
    @Transactional
    private void handlePaymentSucceeded(Event event){
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject().orElseThrow();

        Payment payment = paymentRepository.findByStripePaymentIntentId(intent.getId())
                .orElseThrow(() -> new BusinessException("Payment not found."));

        //O stripe pode enviar o mesmo evento mais de uma vez - IF aqui ignora se já foi processado;
        if (payment.getStatus() == PaymentStatus.SUCCEEDED) return;

        payment.setStatus(PaymentStatus.SUCCEEDED);
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        //Confirma a reserva
        Reservation reservation = payment.getReservation();
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        //Envia o e-mail de confirmação
        emailService.sendReservationConfirmation(reservation);
    }

    @Transactional
    private void handlePaymentFailed(Event event) {
        PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject().orElseThrow();

        Payment payment = paymentRepository.findByStripePaymentIntentId(intent.getId())
                .orElseThrow(() -> new BusinessException("Payment not found."));

        //Idempotência
        if (payment.getStatus() == PaymentStatus.FAILED) return;

        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        //Cancela e libera os assentos;
        Reservation reservation = payment.getReservation();
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        //deleta os reservations_seats para liberar os assentos
        reservationSeatRepository.deleteByReservationId(reservation.getId());
    }
}
