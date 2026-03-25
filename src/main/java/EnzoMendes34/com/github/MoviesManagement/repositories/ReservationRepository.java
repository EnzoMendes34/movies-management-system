package EnzoMendes34.com.github.MoviesManagement.repositories;

import EnzoMendes34.com.github.MoviesManagement.models.Reservation;
import EnzoMendes34.com.github.MoviesManagement.types.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Page<Reservation> findByUserId(Long userId, Pageable pageable);
    List<Reservation> findByStatusAndCreatedAtBefore(
            ReservationStatus status,
            LocalDateTime dateTime
    );

    @Modifying
    @Query("""
    UPDATE Reservation r
        SET r.status = 'CANCELLED'
            WHERE r.status = 'PENDING'
                AND r.createdAt < :limit
    """)
    void expireReservations(@Param("limit") LocalDateTime limit);
}
