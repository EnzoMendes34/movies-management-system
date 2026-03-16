package EnzoMendes34.com.github.MoviesManagement.repositories;

import EnzoMendes34.com.github.MoviesManagement.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByStripePaymentIntentId(String stripePaymentIntentId);
    Optional<Payment> findByReservationId(Long reservationId);
}
