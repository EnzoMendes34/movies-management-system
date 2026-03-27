package EnzoMendes34.com.github.MoviesManagement.services;

import EnzoMendes34.com.github.MoviesManagement.data.dto.SeatDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.BusinessException;
import EnzoMendes34.com.github.MoviesManagement.exceptions.NullObjectException;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ResourceNotFoundException;
import EnzoMendes34.com.github.MoviesManagement.mapper.ObjectMapper;
import EnzoMendes34.com.github.MoviesManagement.models.Room;
import EnzoMendes34.com.github.MoviesManagement.models.Seat;
import EnzoMendes34.com.github.MoviesManagement.repositories.RoomRepository;
import EnzoMendes34.com.github.MoviesManagement.repositories.SeatRepository;
import EnzoMendes34.com.github.MoviesManagement.utils.ValidationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SeatService {

    private final SeatRepository repository;
    private final RoomRepository roomRepository;

    public SeatService(SeatRepository repository, RoomRepository roomRepository) {
        this.repository = repository;
        this.roomRepository = roomRepository;
    }

    //findById(Long id)
    public SeatDTO findById(Long id){
        ValidationUtils.validateRequiredFields(Map.of(
                "id", id
        ));

        return ObjectMapper.parseObject(repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("No seats found for this id;")),
                SeatDTO.class);
    }

    //findByRoomId(Long roomId)
    public List<SeatDTO> findByRoomId(Long roomId) {
        ValidationUtils.validateRequiredFields(Map.of(
                "roomId", roomId
        ));

        return ObjectMapper.parseListObjects(repository.findByRoomId(roomId), SeatDTO.class);
    }

    //findAvailableSeats(Long sessionId, Long roomId)
    public List<SeatDTO> findAvailableSeats(Long sessionId, Long roomId) {
        ValidationUtils.validateRequiredFields(Map.of(
                "sessionId", sessionId,
                "roomId", roomId
        ));

        return ObjectMapper.parseListObjects(repository.findAvailableSeatsBySessionAndRoom(sessionId, roomId) , SeatDTO.class);
    }

    //create(SeatDTO)
    public  SeatDTO create(SeatDTO dto){
        if(dto == null) {
            throw new NullObjectException("It is not possible to save a null object");
        }

        ValidationUtils.validateRequiredFields(Map.of(
                "roomId", dto.getRoomId(),
                "seatNumber", dto.getSeatNumber(),
                "rowLetter", dto.getRowLetter(),
                "type", dto.getType()
        ));

        if(dto.getSeatNumber() <= 0){
            throw new BusinessException("Seat number must be greater than zero.");
        }

        if(!Character.isLetter(dto.getRowLetter())) {
            throw new BusinessException("Row must be a letter.");
        }
        Seat seat = new Seat();

        updateEntityFromDTO(seat, dto);

        return ObjectMapper.parseObject(repository.save(seat), SeatDTO.class);
    }

    //update(SeatDTO)
    public SeatDTO update(SeatDTO dto) {
        if(dto == null) {
            throw new NullObjectException("It is not possible to save a null object");
        }

        ValidationUtils.validateRequiredFields(Map.of(
                "id", dto.getId(),
                "roomId", dto.getRoomId(),
                "seatNumber", dto.getSeatNumber(),
                "rowLetter", dto.getRowLetter(),
                "type", dto.getType()
        ));

        Seat seat = repository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found for this id"));

        updateEntityFromDTO(seat, dto);

        return ObjectMapper.parseObject(repository.save(seat), SeatDTO.class);
    }
    
    //delete(LongId)
    public void delete(Long id){
        ValidationUtils.validateRequiredFields(Map.of(
                "id", id
        ));
        
        Seat entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No seats found for the given Id"));
        
        repository.delete(entity);
    }

    private void updateEntityFromDTO(Seat seat, SeatDTO dto){
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        seat.setSeatNumber(dto.getSeatNumber());
        seat.setRoom(room);
        seat.setType(dto.getType());
        seat.setRowLetter(dto.getRowLetter());
    }
}
