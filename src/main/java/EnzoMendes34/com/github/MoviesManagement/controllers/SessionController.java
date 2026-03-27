package EnzoMendes34.com.github.MoviesManagement.controllers;

import EnzoMendes34.com.github.MoviesManagement.controllers.docs.SessionControllerDocs;
import EnzoMendes34.com.github.MoviesManagement.data.dto.SessionDTO;
import EnzoMendes34.com.github.MoviesManagement.services.SessionService;
import EnzoMendes34.com.github.MoviesManagement.types.SessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/session/v1")
public class SessionController implements SessionControllerDocs {

    private final SessionService service;

    public SessionController(SessionService service) { this.service = service; }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<Page<SessionDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ){
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping(value = "/{id}",
    produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<SessionDTO> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping(value = "/movie/{movieId}",
    produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<List<SessionDTO>> findByMovieIdAndStatus(
            @PathVariable("movieId") Long movieId,
            @RequestParam(value = "status") SessionStatus status){
        return ResponseEntity.ok(service.findByMovieIdAndStatus(movieId, status));
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<SessionDTO> create(@RequestBody SessionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<SessionDTO> update(@RequestBody SessionDTO dto){
        return ResponseEntity.ok(service.update(dto));
    }

    @PatchMapping(value = "/{sessionId}/cancel",
    produces = {MediaType.APPLICATION_JSON_VALUE ,MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public ResponseEntity<SessionDTO> cancelSession(@PathVariable("sessionId") Long sessionId){
        return ResponseEntity.ok(service.cancelSession(sessionId));
    }
}
