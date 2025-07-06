package entities;

import java.util.*;

public class Patron {
    private String patronId;
    private String username;
    private String passwordHash; // In a real system, this would be salted and hashed securely
    private String email;
    private String shippingAddress;
    private Map<String, String> paymentInfo; // Stored as tokenized info in real systems
    private Map<String, Integer> cart; // {bookId: quantity}
    private List<String> orderHistory; // List of orderIds

    public Patron(String username, String passwordHash, String email, String shippingAddress) {
        this.patronId = UUID.randomUUID().toString();
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.shippingAddress = shippingAddress;
        this.paymentInfo = new HashMap<>();
        this.cart = new HashMap<>();
        this.orderHistory = new ArrayList<>();
    }

    // Getters
    public String getPatronId() { return patronId; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getEmail() { return email; }
    public String getShippingAddress() { return shippingAddress; }
    public Map<String, String> getPaymentInfo() { return new HashMap<>(paymentInfo); } // Return a copy
    public Map<String, Integer> getCart() { return new HashMap<>(cart); } // Return a copy
    public List<String> getOrderHistory() { return new ArrayList<>(orderHistory); } // Return a copy

    // Setters (for updates)
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public void setPaymentInfo(Map<String, String> paymentInfo) { this.paymentInfo = new HashMap<>(paymentInfo); }
    public void addOrderToHistory(String orderId) { this.orderHistory.add(orderId); }

    public void updateProfile(Map<String, String> newDetails) {
        newDetails.forEach((key, value) -> {
            switch (key) {
                case "email": this.email = value; break;
                case "shippingAddress": this.shippingAddress = value; break;
                // Add more fields as needed, handle password separately for hashing
            }
        });
        System.out.println("Patron " + username + " profile updated.");
    }

    public void addToCart(String bookId, int quantity) {
        cart.merge(bookId, quantity, Integer::sum);
        System.out.println("Added " + quantity + " of " + bookId + " to cart.");
    }

    public void removeFromCart(String bookId) {
        if (cart.containsKey(bookId)) {
            cart.remove(bookId);
            System.out.println("Removed " + bookId + " from cart.");
        } else {
            System.out.println(bookId + " not in cart.");
        }
    }

    public void clearCart() {
        this.cart.clear();
    }

    @Override
    public String toString() {
        return "Patron{" +
                "patronId='" + patronId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
