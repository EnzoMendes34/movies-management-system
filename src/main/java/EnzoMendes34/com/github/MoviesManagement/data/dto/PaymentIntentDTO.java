package EnzoMendes34.com.github.MoviesManagement.data.dto;


public class PaymentIntentDTO {

    private String clientSecret;

    public PaymentIntentDTO(String clientSecret){
        this.clientSecret = clientSecret;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
