package EnzoMendes34.com.github.MoviesManagement.controllers;

import EnzoMendes34.com.github.MoviesManagement.controllers.docs.SeatControllerDocs;
import EnzoMendes34.com.github.MoviesManagement.data.dto.SeatDTO;
import EnzoMendes34.com.github.MoviesManagement.services.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat/v1")
public class SeatController implements SeatControllerDocs {

    private final SeatService service;

    public SeatController(SeatService service) {
        this.service = service;
    }

    @GetMapping( value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<SeatDTO> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping(value = "/room/{roomId}",
        produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<List<SeatDTO>> findByRoomId(@PathVariable("roomId") Long roomId){
        return ResponseEntity.ok(service.findByRoomId(roomId));
    }

    @GetMapping(value = "/sessions/available",
    produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<List<SeatDTO>> findAllAvailable(
            @RequestParam(value = "sessionId") Long sessionId,
            @RequestParam(value = "roomId") Long roomId){
        return ResponseEntity.ok(service.findAvailableSeats(sessionId, roomId));
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<SeatDTO> create(@RequestBody SeatDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<SeatDTO> update(@RequestBody SeatDTO dto){
        return ResponseEntity.ok(service.update(dto));
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<Void> deleteSeat(@PathVariable("id") Long id){
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
