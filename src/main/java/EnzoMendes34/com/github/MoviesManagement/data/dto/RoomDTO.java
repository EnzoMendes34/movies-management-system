package EnzoMendes34.com.github.MoviesManagement.data.dto;

import EnzoMendes34.com.github.MoviesManagement.types.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Objects;

@Schema(description = "Representação de uma sala de cinema")
public class RoomDTO {

    @Schema(description = "ID da sala", example = "1")
    private Long id;

    @Schema(description = "Nome da sala", example = "Sala 1 - IMAX")
    private String roomName;

    @Schema(description = "Tipo da sala", example = "IMAX")
    private RoomType type;

    @Schema(description = "Capacidade total de assentos", example = "120")
    private int capacity;

    @Schema(description = "Indica se a sala está ativa", example = "true")
    private boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
        RoomDTO roomDTO = (RoomDTO) o;
        return capacity == roomDTO.capacity && enabled == roomDTO.enabled && Objects.equals(id, roomDTO.id) && Objects.equals(roomName, roomDTO.roomName) && type == roomDTO.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomName, type, capacity, enabled);
    }
}
