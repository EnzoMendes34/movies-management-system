package EnzoMendes34.com.github.MoviesManagement.services.scheduler;

import EnzoMendes34.com.github.MoviesManagement.services.ReservationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationScheduler {

    private final ReservationService service;

    public ReservationScheduler(ReservationService service) {
        this.service = service;
    }

    @Scheduled(fixedDelay = 60000)//1 min
    public void expirePendingReservationsJob() {
        service.expirePendingReservations(15);
    }
}
