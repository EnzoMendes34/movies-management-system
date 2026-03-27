package EnzoMendes34.com.github.MoviesManagement.data.dto;

import EnzoMendes34.com.github.MoviesManagement.types.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Schema(description = "Representação de uma reserva")
public class ReservationDTO {

    @Schema(description = "ID da reserva", example = "1")
    private Long id;

    @Schema(description = "ID do usuário", example = "5")
    private Long userId;

    @Schema(description = "Nome completo do usuário", example = "Enzo Mendes")
    private String userFullName;

    @Schema(description = "ID da sessão", example = "10")
    private Long sessionId;

    @Schema(description = "Preço total em centavos", example = "4500")
    private int totalPriceInCents;

    @Schema(description = "Status da reserva", example = "PENDING")
    private ReservationStatus status;

    @Schema(description = "Data de criação")
    private LocalDateTime createdAt;

    @Schema(description = "Lista de IDs dos assentos", example = "[1,2,3]")
    private List<Long> seatsIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public int getTotalPriceInCents() {
        return totalPriceInCents;
    }

    public void setTotalPriceInCents(int totalPriceInCents) {
        this.totalPriceInCents = totalPriceInCents;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Long> getSeatsIds() { return seatsIds; }

    public void setSeatsIds(List<Long> seatsIds) { this.seatsIds = seatsIds; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ReservationDTO that = (ReservationDTO) o;
        return totalPriceInCents == that.totalPriceInCents && Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(userFullName, that.userFullName) && Objects.equals(sessionId, that.sessionId) && status == that.status && Objects.equals(createdAt, that.createdAt) && Objects.equals(seatsIds, that.seatsIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, userFullName, sessionId, totalPriceInCents, status, createdAt, seatsIds);
    }
}
