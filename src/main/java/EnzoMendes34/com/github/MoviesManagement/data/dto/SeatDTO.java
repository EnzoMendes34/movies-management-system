package EnzoMendes34.com.github.MoviesManagement.data.dto;

import EnzoMendes34.com.github.MoviesManagement.types.SeatType;

import java.util.Objects;

public class SeatDTO{

    private Long id;
    private Long roomId;
    private Character rowLetter;
    private int seatNumber;
    private SeatType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Character getRowLetter() {
        return rowLetter;
    }

    public void setRowLetter(Character rowLetter) {
        this.rowLetter = rowLetter;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public SeatType getType() {
        return type;
    }

    public void setType(SeatType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SeatDTO seatDTO = (SeatDTO) o;
        return seatNumber == seatDTO.seatNumber && Objects.equals(id, seatDTO.id) && Objects.equals(roomId, seatDTO.roomId) && Objects.equals(rowLetter, seatDTO.rowLetter) && type == seatDTO.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomId, rowLetter, seatNumber, type);
    }
}
