package EnzoMendes34.com.github.MoviesManagement.data.dto.request;

import java.util.List;
import java.util.Objects;

public class ReservationRequestDTO {

    private Long userId;
    private Long sessionId;
    private List<Long> seatIds;

    public ReservationRequestDTO() { }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ReservationRequestDTO that = (ReservationRequestDTO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(sessionId, that.sessionId) && Objects.equals(seatIds, that.seatIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, sessionId, seatIds);
    }
}
