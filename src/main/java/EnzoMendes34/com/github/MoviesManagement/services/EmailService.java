package EnzoMendes34.com.github.MoviesManagement.services;

import EnzoMendes34.com.github.MoviesManagement.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${email.from}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    //manda um e-mail de confirmação, é chamado pelo payment service após a confirmação do Stripe. Manda os detalhes também
    public void sendReservationConfirmation(Reservation reservation){
        String to = reservation.getUser().getEmail();
        String subject = "Reserva confirmada!";
        String body = buildConfirmationBody(reservation);

        sendEmail(to, subject, body);
    }

    public void sendReservationCancellation(Reservation reservation){
        String to = reservation.getUser().getEmail();
        String subject = "Reserva cancelada";
        String body = buildCancellationBody(reservation);

        sendEmail(to, subject, body);
    }

    //Método que envia o e-mail, chamado por todos os outros métodos
    private void sendEmail(String to, String subject, String body){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
        } catch (Exception e){
            //Tratado dessa forma pois o e-mail não pode derrubar o fluxo principal em caso de falha;
            System.err.println("Error sending e-mail: " + e.getMessage());
        }
    }

    //helpers e builders
    private String buildConfirmationBody(Reservation reservation) {
        var session = reservation.getSession();
        var movie = session.getMovie();

        return """
            Olá, %s!
            
            Sua reserva foi confirmada com sucesso! 🎬
            
            📋 Detalhes da reserva:
            ─────────────────────────────
            Filme: %s
            Data: %s
            Horário: %s
            Sala: %s
            Idioma: %s
            
            💰 Total pago: R$ %.2f
            
            Apresente este e-mail na entrada.
            Boa sessão! 🍿
            """.formatted(
                reservation.getUser().getFullName(),
                movie.getTitle(),
                session.getStartTime().toLocalDate(),
                session.getStartTime().toLocalTime(),
                session.getRoom().getRoomName(),
                session.getLanguage(),
                reservation.getTotalPriceInCents() / 100.0
        );
    }

    private String buildCancellationBody(Reservation reservation){
        return """
            Olá, %s!
            
            Infelizmente sua reserva foi cancelada.
            
            Motivo: pagamento não confirmado ou reserva expirada.
            
            Se isso foi um engano, tente novamente no site.
            
            Atenciosamente,
            Cinema API 🎬
            """.formatted(reservation.getUser().getFullName());
    }
}
