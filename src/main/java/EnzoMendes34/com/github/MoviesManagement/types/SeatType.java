package EnzoMendes34.com.github.MoviesManagement.types;

public enum SeatType {
    STANDARD(1.0),
    VIP(1.2),
    WHEELCHAIR(1.5);

    private final double multiplier;

    SeatType(double multiplier){
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
