package strategy;

import java.util.Map;

public class CreditCardPayment implements IPaymentStrategy {

    @Override
    public boolean processPayment(double amount, Map<String, String> paymentDetails) {
        System.out.println("Processing credit card payment of $" + String.format("%.2f", amount) +
                " for card ending in " + paymentDetails.getOrDefault("lastFourDigits", "N/A") + "...");
        // Simulate interaction with a payment gateway API
        if (amount > 0 && "123".equals(paymentDetails.get("cvv"))) { // Dummy check
            System.out.println("Credit card payment successful.");
            return true;
        }
        System.out.println("Credit card payment failed.");
        return false;
    }
}
