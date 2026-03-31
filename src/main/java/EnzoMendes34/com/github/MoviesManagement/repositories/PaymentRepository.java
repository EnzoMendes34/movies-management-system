package EnzoMendes34.com.github.MoviesManagement.repositories;

import EnzoMendes34.com.github.MoviesManagement.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
    Optional<Payment> findByReservationId(Long reservationId);

    @Query("SELECT p FROM Payment p " +
    "JOIN FETCH p.reservation r " +
    "JOIN FETCH r.user " +
    "WHERE r.id = :reservationId")
    Optional<Payment> findByReservationIdWithDetails(@Param("reservationId") Long reservationId);
}
