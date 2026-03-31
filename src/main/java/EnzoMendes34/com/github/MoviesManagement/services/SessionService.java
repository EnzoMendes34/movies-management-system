package EnzoMendes34.com.github.MoviesManagement.services;

import EnzoMendes34.com.github.MoviesManagement.controllers.SeatController;
import EnzoMendes34.com.github.MoviesManagement.controllers.SessionController;
import EnzoMendes34.com.github.MoviesManagement.data.dto.SessionDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.BusinessException;
import EnzoMendes34.com.github.MoviesManagement.exceptions.NullObjectException;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ResourceNotFoundException;
import EnzoMendes34.com.github.MoviesManagement.mapper.ObjectMapper;
import EnzoMendes34.com.github.MoviesManagement.models.Session;
import EnzoMendes34.com.github.MoviesManagement.repositories.MovieRepository;
import EnzoMendes34.com.github.MoviesManagement.repositories.RoomRepository;
import EnzoMendes34.com.github.MoviesManagement.repositories.SessionRepository;
import EnzoMendes34.com.github.MoviesManagement.types.SessionStatus;
import EnzoMendes34.com.github.MoviesManagement.utils.ValidationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class SessionService {

    private final SessionRepository repository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    public SessionService(SessionRepository repository,
                          MovieRepository movieRepository,
                          RoomRepository roomRepository) {
        this.repository = repository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
    }

    //findAll(Pageable)
    public Page<SessionDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(
                session -> {
                    SessionDTO dto = toDto(session);
                    addHateoasLinks(dto);

                    return dto;
                });
    }

    //findById(Long id)
    public SessionDTO findById(Long id){
        ValidationUtils.validateRequiredFields(Map.of(
                "id", id
        ));

        SessionDTO dto = toDto(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found for the given Id")));

        addHateoasLinks(dto);

        return dto;
    }

    //findByMovieId(Long movieId, SessionStatus status)
    public List<SessionDTO> findByMovieIdAndStatus(Long movieId, SessionStatus status) {
        ValidationUtils.validateRequiredFields(Map.of(
                "movieId", movieId,
                "sessionStatus", status
        ));

        List<SessionDTO> sessions = repository.findByMovieIdAndStatus(movieId, status)
                .stream().map(this::toDto).toList();

        sessions.forEach(this::addHateoasLinks);

        return sessions;
    }

    //create(SessionDTO)
    public SessionDTO create(SessionDTO dto) {
        if (dto == null){
            throw new NullObjectException("It is not possible to create a null object.");
        }

        if(dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new NullObjectException("Start time and end time must not be null");
        }

        ValidationUtils.validateRequiredFields(Map.of(
                "movieId", dto.getMovieId(),
                "roomId", dto.getRoomId(),
                "startTime", dto.getStartTime(),
                "endTime", dto.getEndTime(),
                "language", dto.getLanguage(),
                "priceInCents", dto.getPriceInCents(),
                "status", dto.getStatus()
        ));

        if(dto.getStartTime().isAfter(dto.getEndTime()) ||
                dto.getStartTime().isEqual(dto.getEndTime())) {
            throw new BusinessException("Start time must before end time.");
        }

        if(dto.getDiscountPercentage() != null && (dto.getDiscountPercentage() < 0 || dto.getDiscountPercentage() > 100)) {
            throw new BusinessException("Discount must be between 0 and 100.");
        }

        boolean hasConflict = repository.existsConflictingSession(
                dto.getRoomId(),
                dto.getStartTime(),
                dto.getEndTime()
                );

        if(hasConflict){
            throw new BusinessException("Room already has a session in this time slot.");
        }

        Session session = new Session();

        updateEntityFromDTO(session, dto);

        SessionDTO savedDto = toDto(repository.save(session));

        addHateoasLinks(savedDto);

        return savedDto;
    }

    //update(SessionDTO)
    public SessionDTO update(SessionDTO dto) {
        if (dto == null){
            throw new NullObjectException("It is not possible to create a null object.");
        }

        if(dto.getStartTime() == null || dto.getEndTime() == null) {
            throw new NullObjectException("Start time and end time must not be null");
        }



        ValidationUtils.validateRequiredFields(Map.of(
                "id", dto.getId(),
                "movieId", dto.getMovieId(),
                "roomId", dto.getRoomId(),
                "startTime", dto.getStartTime(),
                "endTime", dto.getEndTime(),
                "language", dto.getLanguage(),
                "priceInCents", dto.getPriceInCents(),
                "status", dto.getStatus()
        ));

        if(dto.getStartTime().isAfter(dto.getEndTime()) ||
                dto.getStartTime().isEqual(dto.getEndTime())) {
            throw new BusinessException("Start time must before end time.");
        }

        if(dto.getDiscountPercentage() != null && (dto.getDiscountPercentage() < 0 || dto.getDiscountPercentage() > 100)) {
            throw new BusinessException("Discount must be between 0 and 100.");
        }


        boolean hasConflict = repository.existsConflictingSessionExcludingThis(dto.getRoomId(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getId());

        if(hasConflict){
            throw new BusinessException("Room already has a session in this time slot.");
        }

        Session session = repository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No session found for the given ID."));

        updateEntityFromDTO(session, dto);

        SessionDTO savedDto = toDto(repository.save(session));

        addHateoasLinks(savedDto);

        return savedDto;
    }

    //cancel(Long id)
    public SessionDTO cancelSession(Long id){
        ValidationUtils.validateRequiredFields(Map.of(
                "id", id
        ));

        Session session = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found for the given id."));

        session.setStatus(SessionStatus.CANCELLED);

        SessionDTO dto = toDto(repository.save(session));

        addHateoasLinks(dto);

        return dto;
    }

    //helpers
    private void updateEntityFromDTO(Session session, SessionDTO dto){
        var movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found in this session."));
        var room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for this session."));

        session.setMovie(movie);
        session.setRoom(room);
        session.setStartTime(dto.getStartTime());
        session.setEndTime(dto.getEndTime());
        session.setLanguage(dto.getLanguage());
        session.setPriceInCents(dto.getPriceInCents());
        session.setStatus(dto.getStatus());
        session.setDiscountPercentage(dto.getDiscountPercentage());
    }

    private void addHateoasLinks(SessionDTO dto){
        dto.add(linkTo(methodOn(SessionController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(SessionController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(SessionController.class).findByMovieIdAndStatus(dto.getMovieId(), dto.getStatus())).withRel("findByMovieIdAndStatus").withType("GET"));
        dto.add(linkTo(methodOn(SeatController.class).findAllAvailable(dto.getId(), dto.getRoomId())).withRel("availableSeatsInSession").withType("GET"));
        dto.add(linkTo(methodOn(SessionController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(SessionController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(SessionController.class).cancelSession(dto.getId())).withRel("cancelSession").withType("PATCH"));
    }

    private SessionDTO toDto(Session session) {
        SessionDTO dto = ObjectMapper.parseObject(session, SessionDTO.class);
        dto.setMovieId(session.getMovie().getId());
        dto.setMovieTitle(session.getMovie().getTitle());
        dto.setRoomId(session.getRoom().getId());
        dto.setRoomName(session.getRoom().getRoomName());

        return dto;
    }
}
