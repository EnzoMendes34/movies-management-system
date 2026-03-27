package EnzoMendes34.com.github.MoviesManagement.data.dto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta contendo o clientSecret para confirmação do pagamento no frontend")
public class PaymentIntentDTO {

    @Schema(description = "Client secret do Stripe", example = "pi_123_secret_abc")
    private String clientSecret;

    public PaymentIntentDTO(String clientSecret){
        this.clientSecret = clientSecret;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
