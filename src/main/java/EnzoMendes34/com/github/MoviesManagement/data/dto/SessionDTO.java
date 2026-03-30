package EnzoMendes34.com.github.MoviesManagement.data.dto;

import EnzoMendes34.com.github.MoviesManagement.types.SessionLanguage;
import EnzoMendes34.com.github.MoviesManagement.types.SessionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Objects;

@Schema(description = "Representa uma sessão de filme com informações de horário, sala e preço.")
public class SessionDTO extends RepresentationModel<SessionDTO> {

    @Schema(
            description = "Identificador único da sessão",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
            description = "ID do filme associado à sessão",
            example = "10"
    )
    private Long movieId;

    @Schema(
            description = "Título do filme",
            example = "Inception",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private String movieTitle;

    @Schema(
            description = "ID da sala onde a sessão será exibida",
            example = "3"
    )
    private Long roomId;

    @Schema(
            description = "Nome da sala",
            example = "Sala A",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private String roomName;

    @Schema(
            description = "Data e horário de início da sessão",
            example = "2026-03-27T18:00:00"
    )
    private LocalDateTime startTime;

    @Schema(
            description = "Data e horário de término da sessão",
            example = "2026-03-27T20:30:00"
    )
    private LocalDateTime endTime;

    @Schema(
            description = "Idioma da sessão",
            example = "DUBBED",
            allowableValues = {"SUBTITLED", "DUBBED"}
    )
    private SessionLanguage language;

    @Schema(
            description = "Preço do ingresso em centavos",
            example = "3000"
    )
    private int priceInCents;

    @Schema(
            description = "Status atual da sessão",
            example = "ACTIVE",
            allowableValues = {"ACTIVE", "CANCELLED"}
    )
    private SessionStatus status;

    @Schema(
            description = "Percentual de desconto aplicado (0 a 100)",
            example = "10",
            nullable = true
    )
    private Integer discountPercentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public SessionLanguage getLanguage() {
        return language;
    }

    public void setLanguage(SessionLanguage language) {
        this.language = language;
    }

    public int getPriceInCents() {
        return priceInCents;
    }

    public void setPriceInCents(int priceInCents) {
        this.priceInCents = priceInCents;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public Integer getDiscountPercentage() { return discountPercentage; }

    public void setDiscountPercentage(Integer discountPercentage) { this.discountPercentage = discountPercentage; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SessionDTO that = (SessionDTO) o;
        return priceInCents == that.priceInCents && Objects.equals(id, that.id) && Objects.equals(movieId, that.movieId) && Objects.equals(movieTitle, that.movieTitle) && Objects.equals(roomId, that.roomId) && Objects.equals(roomName, that.roomName) && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime) && language == that.language && status == that.status && Objects.equals(discountPercentage, that.discountPercentage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movieId, movieTitle, roomId, roomName, startTime, endTime, language, priceInCents, status, discountPercentage);
    }
}
