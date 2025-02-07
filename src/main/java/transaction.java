import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Transaction {

    private final int transactionId;
    private final Customer customer;
    private final Car car;
    private final LocalDate startDate;
    private LocalDate endDate;
    private double totalCost;

    public Transaction(
        int transactionId,
        Customer customer,
        Car car,
        LocalDate startDate
    ) {
        this.transactionId = transactionId;
        this.customer = customer;
        this.car = car;
        this.startDate = startDate;
    }

    public double calculateTotalCost() {
        if (endDate == null) {
            throw new IllegalStateException("End date not set");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                "Start date must be before end date"
            );
        }
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return days * car.getDailyRate();
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Car getCar() {
        return car;
    }
}
