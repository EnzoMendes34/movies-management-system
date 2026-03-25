package EnzoMendes34.com.github.MoviesManagement.services;

import EnzoMendes34.com.github.MoviesManagement.data.dto.ReservationDTO;
import EnzoMendes34.com.github.MoviesManagement.data.dto.request.ReservationRequestDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.BusinessException;
import EnzoMendes34.com.github.MoviesManagement.exceptions.NullObjectException;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ResourceNotFoundException;
import EnzoMendes34.com.github.MoviesManagement.mapper.ObjectMapper;
import EnzoMendes34.com.github.MoviesManagement.models.*;
import EnzoMendes34.com.github.MoviesManagement.repositories.*;
import EnzoMendes34.com.github.MoviesManagement.types.ReservationStatus;
import EnzoMendes34.com.github.MoviesManagement.utils.ValidationUtils;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ReservationService  {

    private final ReservationRepository reservationRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final SeatRepository seatRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationSeatRepository reservationSeatRepository,
                              SeatRepository seatRepository,
                              SessionRepository sessionRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationSeatRepository = reservationSeatRepository;
        this.seatRepository = seatRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    //createReservation(ReservationRequestDTO)
    @Transactional
    public ReservationDTO createReservation(ReservationRequestDTO requestDTO) {
        validateRequest(requestDTO);

        User user = findUser(requestDTO.getUserId());

        Session session = findSession(requestDTO.getSessionId());

        List<Seat> seatsForReservation = getAndValidateSeats(requestDTO.getSeatIds(), session);

        int totalPriceInReservation = calculateTotalReservationPrice(seatsForReservation, session);

        Reservation persistedReservation = createAndSetReservation(user, session, totalPriceInReservation);

        List<ReservationSeat> reservedSeats = createReservationSeats(seatsForReservation, persistedReservation, session);

        reservationSeatRepository.saveAll(reservedSeats);

        ReservationDTO dto = ObjectMapper.parseObject(persistedReservation, ReservationDTO.class);
        dto.setSeatsIds(requestDTO.getSeatIds());
        dto.setUserFullName(user.getFullName());
        dto.setSessionId(session.getId());

        return dto;
    }

    //findById(Long id)
    public ReservationDTO findById(Long id){
        ValidationUtils.validateRequiredFields(Map.of(
                "id", id
        ));

        return ObjectMapper.parseObject(reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found for the given id.")), ReservationDTO.class);
    }

    //findByUser(Long userId)
    public Page<ReservationDTO> findByUser(Long userId, Pageable pageable){
        ValidationUtils.validateRequiredFields(Map.of(
                "userId", userId
        ));

        return reservationRepository.findByUserId(userId, pageable)
                .map(r -> ObjectMapper.parseObject(r, ReservationDTO.class));
    }

    //cancelReservation(Long reservationId)
    @Transactional
    public ReservationDTO cancelReservation(Long id){

        ValidationUtils.validateRequiredFields(Map.of(
                "reservationId", id
        ));

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found."));

        if(reservation.getStatus() == ReservationStatus.CANCELLED){
            throw new BusinessException("Reservation is already cancelled.");
        }

        if(reservation.getStatus() == ReservationStatus.CONFIRMED){
            throw new BusinessException("Confirmed reservations cannot be cancelled.");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);

        return ObjectMapper.parseObject(reservationRepository.save(reservation), ReservationDTO.class);
    }

    //expirePendingReservations(int minutes) expira os acentos reservados por mais de 15 min
    @Transactional
    public void expirePendingReservations(int minutes) {
        LocalDateTime limit = LocalDateTime.now().minusMinutes(minutes);

        List<Reservation> expired = reservationRepository.findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, limit);

        for(Reservation r : expired){
            reservationSeatRepository.deleteByReservationId(r.getId());
        }

        reservationRepository.expireReservations(limit);
    }

    //helpers
    private void validateRequest(ReservationRequestDTO requestDTO){
        if(requestDTO == null) throw new NullObjectException("Request must not be null");

        ValidationUtils.validateRequiredFields(Map.of(
                "userId", requestDTO.getUserId(),
                "sessionId", requestDTO.getSessionId()
        ));

        if(requestDTO.getSeatIds() == null || requestDTO.getSeatIds().isEmpty()){
            throw new BusinessException("Seat list must not be null or empty.");
        }
    }

    private @NonNull User findUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    private Session findSession(Long id){
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found."));
    }

    private List<Seat> getAndValidateSeats(List<Long> seatIds, Session session){

        List<Seat> seatsForReservation = seatRepository.findByIdInWithLock(seatIds);

        if(seatIds.size() != seatsForReservation.size()) {
            throw new BusinessException("One or more seats do not exist.");
        }

        List<Long> reservedSeatIds = reservationSeatRepository.findReservedSeatIds(session.getId(), seatIds);

        if(!reservedSeatIds.isEmpty()){
            throw new BusinessException("One or more seats are already reserved");
        }

        for(Seat seat : seatsForReservation) {
            if(!Objects.equals(seat.getRoom().getId(), session.getRoom().getId())){
                throw new BusinessException("Seat does not belong to the session room");
            }
        }

        return seatsForReservation;
    }

    private int calculateTotalReservationPrice(List<Seat> seats, Session session){
        int totalPriceInReservation = 0;

        for(Seat seat : seats){
            double multiplier = seat.getType().getMultiplier();

            double priceWithType = session.getPriceInCents() * multiplier;

            Integer discount = session.getDiscountPercentage();

            if(discount != null){
                priceWithType = priceWithType * (1 - (discount / 100.0));
            }

            int finalPrice = (int) priceWithType;

            totalPriceInReservation += finalPrice;
        }

        return totalPriceInReservation;
    }

    private Reservation createAndSetReservation(User user, Session session, int total){
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setSession(session);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setTotalPriceInCents(total);
        return reservationRepository.save(reservation);
    }

    private List<ReservationSeat> createReservationSeats(
            List<Seat> seats,
            Reservation reservation,
            Session session
    ){
        List<ReservationSeat> reservedSeats = new ArrayList<>();

        for (Seat seat : seats) {
            ReservationSeat rs = new ReservationSeat();
            rs.setReservation(reservation);
            rs.setSeat(seat);
            rs.setSession(session);

            reservedSeats.add(rs);
        }

        return reservedSeats;
    }
}
