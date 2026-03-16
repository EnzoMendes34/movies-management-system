package EnzoMendes34.com.github.MoviesManagement.repositories;

import EnzoMendes34.com.github.MoviesManagement.models.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
    boolean existsBySeatIdAndSessionId(Long seatId, Long sessionId);
    void deleteByReservationId(Long reservationId);
}
