package EnzoMendes34.com.github.MoviesManagement.models;

import EnzoMendes34.com.github.MoviesManagement.types.SeatType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "seats")
public class Seat implements Serializable {

    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "row_letter", nullable = false, columnDefinition = "CHAR(1)")
    private Character rowLetter;

    @Column(name = "seat_number", nullable = false)
    private int seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType type;

    public Long getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
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
        Seat seat = (Seat) o;
        return seatNumber == seat.seatNumber && Objects.equals(id, seat.id) && Objects.equals(room, seat.room) && Objects.equals(rowLetter, seat.rowLetter) && type == seat.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, room, rowLetter, seatNumber, type);
    }
}
