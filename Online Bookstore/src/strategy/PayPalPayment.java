package strategy;

import java.util.Map;

public class PayPalPayment implements IPaymentStrategy {

    @Override
    public boolean processPayment(double amount, Map<String, String> paymentDetails) {
        System.out.println("Processing PayPal payment of $" + String.format("%.2f", amount) +
                " for email " + paymentDetails.getOrDefault("paypalEmail", "N/A") + "...");
        // Simulate interaction with PayPal API
        if (amount > 0 && paymentDetails.containsKey("paypalEmail") && paymentDetails.get("paypalEmail").contains("@")) { // Dummy check
            System.out.println("PayPal payment successful.");
            return true;
        }
        System.out.println("PayPal payment failed.");
        return false;
    }
}
