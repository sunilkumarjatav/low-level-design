package entities;

import java.util.UUID;

public class OrderItem {
    private String orderItemId;
    private String bookId;
    private int quantity;
    private double priceAtPurchase;

    public OrderItem(String bookId, int quantity, double priceAtPurchase) {
        this.orderItemId = UUID.randomUUID().toString();
        this.bookId = bookId;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    // Getters
    public String getOrderItemId() { return orderItemId; }
    public String getBookId() { return bookId; }
    public int getQuantity() { return quantity; }
    public double getPriceAtPurchase() { return priceAtPurchase; }
}

