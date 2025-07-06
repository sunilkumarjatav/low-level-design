package entities;

import enums.OrderStatus;
import enums.PaymentStatus;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Order {
    private String orderId;
    private String patronId;
    private LocalDateTime orderDate;
    private List<OrderItem> items;
    private double totalAmount;
    private OrderStatus status;
    private String shippingAddress;
    private PaymentStatus paymentStatus;

    public Order(String patronId, String shippingAddress, List<OrderItem> items) {
        this.orderId = UUID.randomUUID().toString();
        this.patronId = patronId;
        this.orderDate = LocalDateTime.now();
        this.items = new ArrayList<>(items); // Create a new list to avoid external modification
        this.totalAmount = calculateTotal();
        this.status = OrderStatus.PENDING;
        this.shippingAddress = shippingAddress;
        this.paymentStatus = PaymentStatus.PENDING;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getPatronId() { return patronId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public List<OrderItem> getItems() { return new ArrayList<>(items); }
    public double getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
    public String getShippingAddress() { return shippingAddress; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }

    private double calculateTotal() {
        return items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPriceAtPurchase())
                .sum();
    }

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
        System.out.println("Order " + this.orderId + " status updated to " + newStatus);
    }

    public void updatePaymentStatus(PaymentStatus newStatus) {
        this.paymentStatus = newStatus;
        System.out.println("Order " + this.orderId + " payment status updated to " + newStatus);
    }

    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("orderId", orderId);
        summary.put("patronId", patronId);
        summary.put("orderDate", orderDate.toString());
        summary.put("items", items.stream()
                .map(item -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("bookId", item.getBookId());
                    itemMap.put("quantity", item.getQuantity());
                    itemMap.put("price", item.getPriceAtPurchase());
                    return itemMap;
                })
                .collect(Collectors.toList()));
        summary.put("totalAmount", totalAmount);
        summary.put("status", status.name());
        summary.put("shippingAddress", shippingAddress);
        summary.put("paymentStatus", paymentStatus.name());
        return summary;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", patronId='" + patronId + '\'' +
                ", status=" + status +
                ", paymentStatus=" + paymentStatus +
                ", totalAmount=" + totalAmount +
                '}';
    }
}