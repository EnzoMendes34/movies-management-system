package EnzoMendes34.com.github.MoviesManagement.data.dto;

import EnzoMendes34.com.github.MoviesManagement.types.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Objects;

@Schema(description = "Resposta com informações do pagamento")
public class PaymentResponseDTO extends RepresentationModel<PaymentResponseDTO> {

    @Schema(description = "ID do pagamento", example = "1")
    private Long id;

    @Schema(description = "ID da reserva", example = "10")
    private Long reservationId;

    @Schema(description = "Valor em centavos", example = "5000")
    private int amountInCents;

    @Schema(description = "Moeda", example = "BRL")
    private String currency;

    @Schema(description = "Status do pagamento", example = "SUCCEEDED")
    private PaymentStatus status;

    @Schema(description = "Data de criação")
    private LocalDateTime createdAt;

    @Schema(description = "Data de pagamento")
    private LocalDateTime paidAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public int getAmountInCents() {
        return amountInCents;
    }

    public void setAmountInCents(int amountInCents) {
        this.amountInCents = amountInCents;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentResponseDTO that = (PaymentResponseDTO) o;
        return amountInCents == that.amountInCents && Objects.equals(id, that.id) && Objects.equals(reservationId, that.reservationId) && Objects.equals(currency, that.currency) && status == that.status && Objects.equals(createdAt, that.createdAt) && Objects.equals(paidAt, that.paidAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reservationId, amountInCents, currency, status, createdAt, paidAt);
    }
}
