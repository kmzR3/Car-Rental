import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.*;

public class CarRentalTest {

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
        assertThrows(IllegalStateException.class, () ->
            agency.rentCar("C1", "T1", LocalDate.now())
        );
    }

    @Test
    void testInvalidReturn() {
        assertThrows(IllegalArgumentException.class, () ->
            agency.returnCar(999, LocalDate.now())
        );
    }
}
