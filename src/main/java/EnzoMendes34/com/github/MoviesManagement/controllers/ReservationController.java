package EnzoMendes34.com.github.MoviesManagement.controllers;

import EnzoMendes34.com.github.MoviesManagement.controllers.docs.ReservationControllerDocs;
import EnzoMendes34.com.github.MoviesManagement.data.dto.ReservationDTO;
import EnzoMendes34.com.github.MoviesManagement.data.dto.request.ReservationRequestDTO;
import EnzoMendes34.com.github.MoviesManagement.services.ReservationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation/v1")
public class ReservationController implements ReservationControllerDocs {

    private final ReservationService service;

    public ReservationController(ReservationService service) { this.service = service; }

    @GetMapping(value = "/{id}",
    produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<ReservationDTO> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping(value = "/user/{userId}",
    produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<Page<ReservationDTO>> findByUserId(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
            ){
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));

        return ResponseEntity.ok(service.findByUser(userId, pageable));
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createReservation(dto));
    }

    @PatchMapping(value = "/{id}/cancel",
    produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.cancelReservation(id));
    }
}
