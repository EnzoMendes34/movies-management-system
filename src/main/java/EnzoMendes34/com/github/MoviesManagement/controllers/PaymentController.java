package EnzoMendes34.com.github.MoviesManagement.controllers;

import EnzoMendes34.com.github.MoviesManagement.controllers.docs.PaymentControllerDocs;
import EnzoMendes34.com.github.MoviesManagement.data.dto.PaymentIntentDTO;
import EnzoMendes34.com.github.MoviesManagement.data.dto.PaymentResponseDTO;
import EnzoMendes34.com.github.MoviesManagement.services.PaymentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment/v1")
public class PaymentController implements PaymentControllerDocs {

    private final PaymentService service;

    public PaymentController(PaymentService service) { this.service = service; }

    @PostMapping(value = "/intent",
            produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<PaymentIntentDTO> createPaymentIntent(
            @RequestParam(name = "reservationId") Long reservationId,
            @RequestParam(name = "userId") Long userId
            ){
        return ResponseEntity.ok(service.createPaymentIntent(reservationId, userId));
    }

    @PostMapping("/webhook")
    @Override
    public ResponseEntity<Void> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
                service.handleWebhook(payload, sigHeader);

                return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/status/{reservationId}",
    produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<PaymentResponseDTO> getPaymentStatus(
            @PathVariable("reservationId") Long reservationId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(service.getPaymentStatus(reservationId, userDetails.getUsername()));
    }
}
