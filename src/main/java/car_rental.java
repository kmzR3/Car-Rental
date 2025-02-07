import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

// Car class
class Car {
    private final String make;
    private final String model;
    private final int year;
    private final String vehicleId;
    private final double dailyRate;
    private boolean isAvailable;

    public Car(String make, String model, int year, String vehicleId, double dailyRate) {
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

    // Getters
    public String getVehicleId() { return vehicleId; }
    public double getDailyRate() { return dailyRate; }
    public boolean isAvailable() { return isAvailable; }
}

// Customer class
class Customer {
    private final String customerId;
    private final String name;
    private final String contactInfo;
    private final List<Transaction> rentalHistory;

    public Customer(String customerId, String name, String contactInfo) {
        this.customerId = customerId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.rentalHistory = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        rentalHistory.add(transaction);
    }

    // Getters
    public String getCustomerId() { return customerId; }
    public List<Transaction> getRentalHistory() { return rentalHistory; }
}

// Transaction class
class Transaction {
    private final int transactionId;
    private final Customer customer;
    private final Car car;
    private final LocalDate startDate;
    private LocalDate endDate;
    private double totalCost;

    public Transaction(int transactionId, Customer customer, Car car, LocalDate startDate) {
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
            throw new IllegalArgumentException("Start date must be before end date");
        }
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return days * car.getDailyRate();
    }

    // Setters and Getters
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public int getTransactionId() { return transactionId; }
    public LocalDate getEndDate() { return endDate; }
    public Car getCar() { return car; }
}

// Rental Agency class
class RentalAgency {
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

    public Transaction rentCar(String customerId, String vehicleId, LocalDate startDate) {
        Customer customer = findCustomerById(customerId);
        Car car = findCarById(vehicleId);

        if (customer == null || car == null) {
            throw new IllegalArgumentException("Customer or car not found");
        }
        if (!car.isAvailable()) {
            throw new IllegalStateException("Car is not available");
        }

        car.rent();
        Transaction transaction = new Transaction(nextTransactionId++, customer, car, startDate);
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
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    private Car findCarById(String vehicleId) {
        return cars.stream()
                .filter(c -> c.getVehicleId().equals(vehicleId))
                .findFirst()
                .orElse(null);
    }

    private Transaction findTransactionById(int transactionId) {
        return transactions.stream()
                .filter(t -> t.getTransactionId() == transactionId)
                .findFirst()
                .orElse(null);
    }
}

// Test cases (JUnit 5)
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class CarRentalTest {
    private RentalAgency agency;
    private Car testCar;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        agency = new RentalAgency();
        testCar = new Car("Toyota", "Camry", 2023, "T1", 50.0);
        testCustomer = new Customer("C1", "John Doe", "john@example.com");

        agency.addCar(testCar);
        agency.registerCustomer(testCustomer);
    }

    @Test
    void testSuccessfulRental() {
        Transaction t = agency.rentCar("C1", "T1", LocalDate.now());
        assertNotNull(t);
        assertFalse(testCar.isAvailable());
    }

    @Test
    void testReturnCarCalculation() {
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 1, 5);
        Transaction t = agency.rentCar("C1", "T1", start);
        double cost = agency.returnCar(t.getTransactionId(), end);

        assertEquals(200.0, cost);
        assertTrue(testCar.isAvailable());
    }

    @Test
    void testRentUnavailableCar() {
        agency.rentCar("C1", "T1", LocalDate.now());
        assertThrows(IllegalStateException.class,
            () -> agency.rentCar("C1", "T1", LocalDate.now()));
    }

    @Test
    void testInvalidReturn() {
        assertThrows(IllegalArgumentException.class,
            () -> agency.returnCar(999, LocalDate.now()));
    }
}
