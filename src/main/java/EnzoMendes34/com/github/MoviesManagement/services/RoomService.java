package EnzoMendes34.com.github.MoviesManagement.services;

import EnzoMendes34.com.github.MoviesManagement.data.dto.RoomDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.NullObjectException;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ResourceNotFoundException;
import EnzoMendes34.com.github.MoviesManagement.mapper.ObjectMapper;
import EnzoMendes34.com.github.MoviesManagement.models.Room;
import EnzoMendes34.com.github.MoviesManagement.repositories.RoomRepository;
import EnzoMendes34.com.github.MoviesManagement.utils.ValidationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) { this.repository = repository; }

    //findAll()
    public Page<RoomDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(
                room -> ObjectMapper.parseObject(room, RoomDTO.class)
        );
    }

    //findAllEnabled()
    public List<RoomDTO> findAllEnabled() {
        return ObjectMapper.parseListObjects(repository.findByEnabledTrue(), RoomDTO.class);
    }

    //findById(Long id)
    public RoomDTO findById(Long id) {
        ValidationUtils.validateRequiredFields(Map.of(
                "id", id
        ));

        return ObjectMapper.parseObject(repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Room not found for this id.")),
                RoomDTO.class);
    }

    //create(RoomDTO dto)
    public RoomDTO create(RoomDTO dto) {
        ValidationUtils.validateRequiredFields(Map.of(
                "roomName", dto.getRoomName(),
                "capacity", dto.getCapacity(),
                "type", dto.getType()
        ));

        Room room = new Room();

        updateEntityFromDTO(room, dto);

        return ObjectMapper.parseObject(
                repository.save(room), RoomDTO.class
        );
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

        return ObjectMapper.parseObject(
                repository.save(room), RoomDTO.class
        );
    }

    //disable(Long id)
    public RoomDTO disable(Long id){
        ValidationUtils.validateRequiredFields(Map.of(
                "id", id
        ));

        Room room = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for this id."));

        room.setEnabled(false);
        return ObjectMapper.parseObject(repository.save(room), RoomDTO.class);
    }

    private void updateEntityFromDTO(Room room, RoomDTO dto){
        room.setRoomName(dto.getRoomName());
        room.setCapacity(dto.getCapacity());
        room.setType(dto.getType());
    }
}
