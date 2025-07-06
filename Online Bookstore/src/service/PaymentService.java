package service;

import strategy.IPaymentStrategy;
import java.util.Map;

public class PaymentService {
    public boolean processTransaction(double amount, Map<String, String> paymentDetails, IPaymentStrategy paymentStrategy) {
        return paymentStrategy.processPayment(amount, paymentDetails);
    }
}
