package EnzoMendes34.com.github.MoviesManagement.services;

import EnzoMendes34.com.github.MoviesManagement.controllers.RoomController;
import EnzoMendes34.com.github.MoviesManagement.data.dto.RoomDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.BusinessException;
import EnzoMendes34.com.github.MoviesManagement.exceptions.NullObjectException;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ResourceNotFoundException;
import EnzoMendes34.com.github.MoviesManagement.mapper.ObjectMapper;
import EnzoMendes34.com.github.MoviesManagement.models.Room;
import EnzoMendes34.com.github.MoviesManagement.models.Seat;
import EnzoMendes34.com.github.MoviesManagement.repositories.RoomRepository;
import EnzoMendes34.com.github.MoviesManagement.repositories.SeatRepository;
import EnzoMendes34.com.github.MoviesManagement.types.SeatType;
import EnzoMendes34.com.github.MoviesManagement.utils.ValidationUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class RoomService {

    private final RoomRepository repository;
    private final SeatRepository seatRepository;

    public RoomService(RoomRepository repository, SeatRepository seatRepository) {
        this.repository = repository;
        this.seatRepository = seatRepository;
    }

    //findAll()
    public Page<RoomDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(
                room -> {
                    RoomDTO dto = ObjectMapper.parseObject(room, RoomDTO.class);
                    addHateoasLinks(dto);

                    return dto;
                });
    }

    //findAllEnabled()
    public List<RoomDTO> findAllEnabled() {
        List<RoomDTO> rooms = ObjectMapper.parseListObjects(repository.findByEnabledTrue(), RoomDTO.class);

        rooms.forEach(this::addHateoasLinks);
        return rooms;
    }

    //findById(Long id)
    public RoomDTO findById(Long id) {
        ValidationUtils.validateRequiredFields(Map.of(
                "id", id
        ));

        RoomDTO dto = ObjectMapper.parseObject(repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Room not found for this id.")),
                RoomDTO.class);

        addHateoasLinks(dto);

        return dto;
    }

    //create(RoomDTO dto)
    @Transactional //garantir que sala e assentos sejam salvos juntos
    public RoomDTO create(RoomDTO dto) {
        ValidationUtils.validateRequiredFields(Map.of(
                "roomName", dto.getRoomName(),
                "capacity", dto.getCapacity(),
                "type", dto.getType()
        ));

        if(dto.getCapacity() <= 0 ){
            throw new BusinessException("Room capacity must be greater than zero");
        }

        Room room = new Room();

        updateEntityFromDTO(room, dto);
        room.setEnabled(true);
        Room savedRoom = repository.save(room);

        generateSeatsForRoom(savedRoom);

        RoomDTO savedDto = ObjectMapper.parseObject(savedRoom, RoomDTO.class);
        addHateoasLinks(savedDto);

        return savedDto;
    }

    //update(RoomDTO dto)
    public RoomDTO update(RoomDTO dto){
        if(dto == null) {
            throw new NullObjectException("It is not possible to save a null object");
        }

        ValidationUtils.validateRequiredFields(Map.of(
                "id", dto.getId(),
                "roomName", dto.getRoomName(),
                "capacity", dto.getCapacity(),
                "type", dto.getType()
        ));

        Room room = repository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for this id."));

        updateEntityFromDTO(room, dto);

        RoomDTO savedDto = ObjectMapper.parseObject(
                repository.save(room), RoomDTO.class
        );

        addHateoasLinks(savedDto);

        return savedDto;
    }

    //disable(Long id)
    public RoomDTO disable(Long id){
        ValidationUtils.validateRequiredFields(Map.of(
                "id", id
        ));

        Room room = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for this id."));

        room.setEnabled(false);
        RoomDTO dto = ObjectMapper.parseObject(repository.save(room), RoomDTO.class);

        addHateoasLinks(dto);

        return dto;
    }

    private void updateEntityFromDTO(Room room, RoomDTO dto){
        room.setRoomName(dto.getRoomName());
        room.setCapacity(dto.getCapacity());
        room.setType(dto.getType());
    }

    private void addHateoasLinks(RoomDTO dto) {
        //findAll, findAllEnabled, findById, create, update, disable
        dto.add(linkTo(methodOn(RoomController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(RoomController.class).findAllEnabled()).withRel("findAllEnabled").withType("GET"));
        dto.add(linkTo(methodOn(RoomController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(RoomController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(RoomController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(RoomController.class).disable(dto.getId())).withRel("disable").withType("PATCH"));
    }

    //Lógica para gerar seats automaticamente com os parâmetros da Room, para não criar seats manualmente
    private void generateSeatsForRoom(Room room) {
        int capacity = room.getCapacity();
        int seatsPerRow = 10; //todo: ajustar DTO e entity para receber isso
        List<Seat> seats = new ArrayList<>();

        char rowLetter = 'A';
        int currentSeatInRow = 1;

        for(int i = 0; i < capacity; i++){
            Seat seat = new Seat();
            seat.setRowLetter(rowLetter);
            seat.setSeatNumber(currentSeatInRow);
            seat.setRoom(room);
            seat.setType(SeatType.STANDARD);

            seats.add(seat);

            if(currentSeatInRow >= seatsPerRow){
                rowLetter++;
                currentSeatInRow = 1;
            } else {
                currentSeatInRow++;
            }
        }
        seatRepository.saveAll(seats);
    }

}
