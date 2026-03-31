package EnzoMendes34.com.github.MoviesManagement.controllers.docs;

import EnzoMendes34.com.github.MoviesManagement.data.dto.PaymentIntentDTO;
import EnzoMendes34.com.github.MoviesManagement.data.dto.PaymentResponseDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

@Tag(name = "Payments", description = "Gerenciamento de pagamentos e integração com Stripe")
public interface PaymentControllerDocs {

    @Operation(
            summary = "Criar intenção de pagamento",
            description = "Cria um PaymentIntent no Stripe e retorna o clientSecret necessário para finalizar o pagamento no frontend."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PaymentIntent criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou acesso negado",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<PaymentIntentDTO> createPaymentIntent(
            @Parameter(description = "ID da reserva", example = "10") Long reservationId,
            @Parameter(description = "ID do usuário", example = "5") Long userId
    );


    @Operation(
            summary = "Webhook do Stripe",
            description = "Endpoint chamado automaticamente pelo Stripe para notificar eventos de pagamento. NÃO deve ser chamado manualmente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento processado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Assinatura inválida",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<Void> handleWebhook(
            @Parameter(description = "Payload enviado pelo Stripe") String payload,
            @Parameter(description = "Header de assinatura do Stripe", example = "t=123,v1=abc...") String sigHeader
    );


    @Operation(
            summary = "Consultar status do pagamento",
            description = "Retorna o status atual do pagamento de uma reserva."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pagamento não encontrado",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    ResponseEntity<PaymentResponseDTO> getPaymentStatus(
            @Parameter(description = "ID da reserva", example = "10") Long reservationId,
            @Parameter(hidden = true) UserDetails userDetails
    );
}