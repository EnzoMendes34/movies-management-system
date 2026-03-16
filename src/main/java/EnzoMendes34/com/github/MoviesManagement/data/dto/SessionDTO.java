package EnzoMendes34.com.github.MoviesManagement.data.dto;

import EnzoMendes34.com.github.MoviesManagement.types.SessionLanguage;
import EnzoMendes34.com.github.MoviesManagement.types.SessionStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class SessionDTO{

    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long roomId;
    private String roomName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SessionLanguage language;
    private int priceInCents;
    private SessionStatus status;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SessionDTO that = (SessionDTO) o;
        return priceInCents == that.priceInCents && Objects.equals(id, that.id) && Objects.equals(movieId, that.movieId) && Objects.equals(movieTitle, that.movieTitle) && Objects.equals(roomId, that.roomId) && Objects.equals(roomName, that.roomName) && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime) && language == that.language && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movieId, movieTitle, roomId, roomName, startTime, endTime, language, priceInCents, status);
    }
}
