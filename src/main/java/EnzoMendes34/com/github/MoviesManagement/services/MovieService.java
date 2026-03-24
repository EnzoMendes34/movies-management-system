package EnzoMendes34.com.github.MoviesManagement.services;

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

@Service
public class MovieService {

    private final MovieRepository repository;

    public MovieService(MovieRepository repository) { this.repository = repository; }

    //findAll()
    public Page<MovieDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(
                movie -> ObjectMapper.parseObject(movie, MovieDTO.class)
        );
    }

    //findById(Long id)
    public MovieDTO findById(Long id) {
        ValidationUtils.validateRequiredFields(Map.of(
                "id", id
        ));

        return ObjectMapper.parseObject(repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Movie not found for this id.")),
                MovieDTO.class);
    }
    //FindByTitle(titulo, Pageable)
    public Page<MovieDTO> findByTitle(String title, Pageable pageable) {
        return repository.findByTitleContainingIgnoreCase(title, pageable).map(
                movie -> ObjectMapper.parseObject(movie, MovieDTO.class)
        );
    }
    //create
    public MovieDTO create(MovieDTO dto) {
        ValidationUtils.validateRequiredFields(Map.of(
                "title", dto.getTitle(),
                "genre", dto.getGenre()
        ));

        Movie movie = new Movie();
        updateEntityFromDTO(movie, dto);


        return ObjectMapper.parseObject(
                repository.save(movie), MovieDTO.class
        );
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

        return ObjectMapper.parseObject(
                repository.save(movie), MovieDTO.class
        );
    }

    //disable(id)
    public MovieDTO disable(Long id){
        ValidationUtils.validateRequiredFields(Map.of(
                "id", id
        ));

        Movie movie = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found for this id."));

        movie.setEnabled(false);
        return ObjectMapper.parseObject(repository.save(movie), MovieDTO.class);
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
}