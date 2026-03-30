package EnzoMendes34.com.github.MoviesManagement.services;

import EnzoMendes34.com.github.MoviesManagement.controllers.MovieController;
import EnzoMendes34.com.github.MoviesManagement.data.dto.MovieDTO;
import EnzoMendes34.com.github.MoviesManagement.exceptions.NullObjectException;
import EnzoMendes34.com.github.MoviesManagement.exceptions.ResourceNotFoundException;
import EnzoMendes34.com.github.MoviesManagement.mapper.ObjectMapper;
import EnzoMendes34.com.github.MoviesManagement.models.Movie;
import EnzoMendes34.com.github.MoviesManagement.repositories.MovieRepository;
import EnzoMendes34.com.github.MoviesManagement.utils.ValidationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class MovieService {

    private final MovieRepository repository;

    public MovieService(MovieRepository repository) { this.repository = repository; }

    //findAll()
    public Page<MovieDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(
                movie -> {
                    MovieDTO dto = ObjectMapper.parseObject(movie, MovieDTO.class);
                    addHateoasLinks(dto);

                    return dto;
                });
    }

    //findById(Long id)
    public MovieDTO findById(Long id) {
        ValidationUtils.validateRequiredFields(Map.of(
                "id", id
        ));

        MovieDTO dto = ObjectMapper.parseObject(repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Movie not found for this id.")),
                MovieDTO.class);

        addHateoasLinks(dto);

        return dto;
    }
    //FindByTitle(titulo, Pageable)
    public Page<MovieDTO> findByTitle(String title, Pageable pageable) {
        return repository.findByTitleContainingIgnoreCase(title, pageable).map(
                movie -> {
                    MovieDTO dto = ObjectMapper.parseObject(movie, MovieDTO.class);
                    addHateoasLinks(dto);

                    return dto;
                });
    }
    //create
    public MovieDTO create(MovieDTO dto) {
        ValidationUtils.validateRequiredFields(Map.of(
                "title", dto.getTitle(),
                "genre", dto.getGenre()
        ));

        Movie movie = new Movie();
        updateEntityFromDTO(movie, dto);


        MovieDTO savedDto = ObjectMapper.parseObject( repository.save(movie), MovieDTO.class);

        addHateoasLinks(savedDto);

        return savedDto;
    }

    //update
    public MovieDTO update(MovieDTO dto){
        if(dto == null) {
            throw new NullObjectException("It is not possible to save a null object");
        }

        ValidationUtils.validateRequiredFields(Map.of(
                "id", dto.getId()
        ));

        Movie movie = repository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found for this id."));

        updateEntityFromDTO(movie, dto);

        MovieDTO updatedDto = ObjectMapper.parseObject(
                repository.save(movie), MovieDTO.class
        );

        addHateoasLinks(updatedDto);

        return updatedDto;
    }

    //disable(id)
    public MovieDTO disable(Long id){
        ValidationUtils.validateRequiredFields(Map.of(
                "id", id
        ));

        Movie movie = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found for this id."));

        movie.setEnabled(false);
        MovieDTO dto = ObjectMapper.parseObject(repository.save(movie), MovieDTO.class);

        addHateoasLinks(dto);

        return dto;
    }

    private void updateEntityFromDTO(Movie movie, MovieDTO dto){
        movie.setTitle(dto.getTitle());
        movie.setGenre(dto.getGenre());
        movie.setRating(dto.getRating());
        movie.setSynopsis(dto.getSynopsis());
        movie.setDurationInMinutes(dto.getDurationInMinutes());
        movie.setPosterUrl(dto.getPosterUrl());
        movie.setReleaseDate(dto.getReleaseDate());
    }

    private void addHateoasLinks(MovieDTO dto) {
        //findAll, findById, findByTitle, create, update, disable
        dto.add(linkTo(methodOn(MovieController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(MovieController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(MovieController.class).findByTitle("", 1, 12, "asc")).withRel("findByTitle").withType("GET"));
        dto.add(linkTo(methodOn(MovieController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(MovieController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(MovieController.class).disableMovie(dto.getId())).withRel("disableMovie").withType("PATCH"));
    }
}