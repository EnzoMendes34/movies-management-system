package EnzoMendes34.com.github.MoviesManagement.data.dto;

import EnzoMendes34.com.github.MoviesManagement.types.RoomType;
import jakarta.persistence.*;

import java.util.Objects;

public class RoomDTO {

    private Long id;
    private String roomName;
    private RoomType type;
    private int capacity;
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
