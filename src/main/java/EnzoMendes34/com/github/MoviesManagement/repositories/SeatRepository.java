package EnzoMendes34.com.github.MoviesManagement.repositories;

import EnzoMendes34.com.github.MoviesManagement.models.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id IN :ids")
    List<Seat> findByIdInWithLock(@Param("ids") List<Long> ids);

    @Query("""
              SELECT s FROM Seat s
              WHERE s.room.id = :roomId
              AND s.id NOT IN (
              SELECT rs.seat.id FROM ReservationSeat rs
              WHERE rs.session.id = :sessionId
)
""")
    List<Seat> findAvailableSeatsBySessionAndRoom(
            @Param("sessionId") Long sessionId,
            @Param("roomId") Long roomId
    );

    @Query("SELECT s FROM Seat s WHERE room.id = :roomId")
    List<Seat> findByRoomId(@Param("roomId") Long roomId);
}
