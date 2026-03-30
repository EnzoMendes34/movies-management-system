package EnzoMendes34.com.github.MoviesManagement.data.dto;

import EnzoMendes34.com.github.MoviesManagement.types.MovieRating;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.Objects;

@Schema(description = "Representação de um filme")
public class MovieDTO extends RepresentationModel<MovieDTO> {

    @Schema(description = "ID do filme", example = "1")
    private Long id;

    @Schema(description = "Título do filme", example = "The Dark Knight")
    private String title;

    @Schema(description = "Sinopse", example = "Batman enfrenta o Coringa em Gotham.")
    private String synopsis;

    @Schema(description = "Duração em minutos", example = "152")
    private int durationInMinutes;

    @Schema(description = "Gênero do filme", example = "Action")
    private String genre;

    @Schema(description = "Classificação indicativa", example = "PG_13")
    private MovieRating rating;

    @Schema(description = "URL do poster", example = "https://image.com/poster.jpg")
    private String posterUrl;

    @Schema(description = "Data de lançamento", example = "2008-07-18")
    private LocalDate releaseDate;

    @Schema(description = "Indica se o filme está ativo", example = "true")
    private boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public MovieRating getRating() {
        return rating;
    }

    public void setRating(MovieRating rating) {
        this.rating = rating;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MovieDTO movieDTO = (MovieDTO) o;
        return durationInMinutes == movieDTO.durationInMinutes && enabled == movieDTO.enabled && Objects.equals(id, movieDTO.id) && Objects.equals(title, movieDTO.title) && Objects.equals(synopsis, movieDTO.synopsis) && Objects.equals(genre, movieDTO.genre) && rating == movieDTO.rating && Objects.equals(posterUrl, movieDTO.posterUrl) && Objects.equals(releaseDate, movieDTO.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, synopsis, durationInMinutes, genre, rating, posterUrl, releaseDate, enabled);
    }
}
