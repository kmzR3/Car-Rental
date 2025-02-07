import java.util.ArrayList;
import java.util.List;

public class Customer {

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

    public String getCustomerId() {
        return customerId;
    }

    public List<Transaction> getRentalHistory() {
        return rentalHistory;
    }
}
