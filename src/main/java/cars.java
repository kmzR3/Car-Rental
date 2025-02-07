import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Car {

    private final String make;
    private final String model;
    private final int year;
    private final String vehicleId;
    private final double dailyRate;
    private boolean isAvailable;

    public Car(
        String make,
        String model,
        int year,
        String vehicleId,
        double dailyRate
    ) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.vehicleId = vehicleId;
        this.dailyRate = dailyRate;
        this.isAvailable = true;
    }

    public void rent() {
        if (!isAvailable) {
            throw new IllegalStateException("Car is not available for rent");
        }
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
