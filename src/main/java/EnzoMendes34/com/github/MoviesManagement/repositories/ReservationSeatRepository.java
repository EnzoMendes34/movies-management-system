package EnzoMendes34.com.github.MoviesManagement.repositories;

import EnzoMendes34.com.github.MoviesManagement.models.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
    boolean existsBySeatIdAndSessionId(Long seatId, Long sessionId);
    void deleteByReservationId(Long reservationId);

    @Query("""
        SELECT rs.seat.id FROM ReservationSeat rs
        WHERE rs.session.id = :sessionId
        AND rs.seat.id IN :seatIds
        AND rs.reservation.status != 'CANCELLED'
    """)
    List<Long> findReservedSeatIds(Long sessionId, List<Long> seatIds);
}
