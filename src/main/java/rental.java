import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalAgency {

    private final List<Car> cars = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();
    private int nextTransactionId = 1;

    public void registerCustomer(Customer customer) {
        customers.add(customer);
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public Transaction rentCar(
        String customerId,
        String vehicleId,
        LocalDate startDate
    ) {
        Customer customer = findCustomerById(customerId);
        Car car = findCarById(vehicleId);

        if (customer == null || car == null) {
            throw new IllegalArgumentException("Customer or car not found");
        }
        if (!car.isAvailable()) {
            throw new IllegalStateException("Car is not available");
        }

        car.rent();
        Transaction transaction = new Transaction(
            nextTransactionId++,
            customer,
            car,
            startDate
        );
        transactions.add(transaction);
        customer.addTransaction(transaction);
        return transaction;
    }

    public double returnCar(int transactionId, LocalDate endDate) {
        Transaction transaction = findTransactionById(transactionId);
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found");
        }
        if (transaction.getEndDate() != null) {
            throw new IllegalStateException("Transaction already closed");
        }

        transaction.setEndDate(endDate);
        double cost = transaction.calculateTotalCost();
        transaction.setTotalCost(cost);
        transaction.getCar().returnCar();
        return cost;
    }

    private Customer findCustomerById(String customerId) {
        return customers
            .stream()
            .filter(c -> c.getCustomerId().equals(customerId))
            .findFirst()
            .orElse(null);
    }

    private Car findCarById(String vehicleId) {
        return cars
            .stream()
            .filter(c -> c.getVehicleId().equals(vehicleId))
            .findFirst()
            .orElse(null);
    }

    private Transaction findTransactionById(int transactionId) {
        return transactions
            .stream()
            .filter(t -> t.getTransactionId() == transactionId)
            .findFirst()
            .orElse(null);
    }
}
