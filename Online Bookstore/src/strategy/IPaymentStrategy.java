package strategy;

import java.util.Map;

public interface IPaymentStrategy {
    boolean processPayment(double amount, Map<String, String> paymentDetails);
}
