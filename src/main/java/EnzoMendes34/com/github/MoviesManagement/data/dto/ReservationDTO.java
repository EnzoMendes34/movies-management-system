package EnzoMendes34.com.github.MoviesManagement.data.dto;

import EnzoMendes34.com.github.MoviesManagement.types.ReservationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ReservationDTO {

    private Long id;
    private Long userId;
    private String userFullName;
    private Long sessionId;
    private int totalPriceInCents;
    private ReservationStatus status;
    private LocalDateTime createdAt;
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
