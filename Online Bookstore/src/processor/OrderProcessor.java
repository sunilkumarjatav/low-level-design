package processor;

import entities.Book;
import entities.Order;
import entities.OrderItem;
import entities.Patron;
import enums.OrderStatus;
import enums.PaymentStatus;
import repository.IBookRepository;
import repository.IOrderRepository;
import service.IInventoryService;
import service.PaymentService;
import strategy.IPaymentStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderProcessor {

    private IBookRepository bookRepository;
    private IOrderRepository orderRepository;
    private IInventoryService inventoryService;
    private PaymentService paymentService;

    public OrderProcessor(IBookRepository bookRepository, IOrderRepository orderRepository,
                          IInventoryService inventoryService, PaymentService paymentService) {
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
    }

    public Optional<Order> placeOrder(Patron patron, String shippingAddress,
                                      Map<String, String> paymentDetails, IPaymentStrategy paymentStrategy) {
        if (patron.getCart().isEmpty()) {
            System.out.println("Cart is empty. Cannot place order.");
            return Optional.empty();
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double totalCalculatedPrice = 0.0;

        // Validate stock and prepare order items
        for (Map.Entry<String, Integer> entry : patron.getCart().entrySet()) {
            String bookId = entry.getKey();
            int quantity = entry.getValue();
            Optional<Book> bookOptional = bookRepository.getBookById(bookId);

            if (bookOptional.isEmpty() || bookOptional.get().getQuantityAvailable() < quantity) {
                System.out.println("Error: Book '" + (bookOptional.isPresent() ? bookOptional.get().getTitle() : bookId) +
                        "' is out of stock or insufficient quantity.");
                return Optional.empty();
            }
            Book book = bookOptional.get();
            orderItems.add(new OrderItem(bookId, quantity, book.getPrice()));
            totalCalculatedPrice += quantity * book.getPrice();
        }

        // Create the order object
        Order newOrder = new Order(patron.getPatronId(), shippingAddress, orderItems);
        // Recalculate total to be absolutely sure (though it's done in Order constructor)
        // newOrder.setTotalAmount(totalCalculatedPrice); // No setter, total is calculated in constructor from items

        // Process payment
        boolean paymentSuccessful = paymentService.processTransaction(
                newOrder.getTotalAmount(), paymentDetails, paymentStrategy
        );

        if (paymentSuccessful) {
            newOrder.updatePaymentStatus(PaymentStatus.PAID);
            newOrder.updateStatus(OrderStatus.PROCESSING);
            orderRepository.addOrder(newOrder);

            // Deduct stock for each item in the order
            for (OrderItem item : orderItems) {
                inventoryService.decrementStock(item.getBookId(), item.getQuantity());
            }

            patron.addOrderToHistory(newOrder.getOrderId());
            patron.clearCart(); // Clear cart after successful order

            System.out.println("Order " + newOrder.getOrderId() + " placed successfully!");
            return Optional.of(newOrder);
        } else {
            newOrder.updatePaymentStatus(PaymentStatus.FAILED);
            newOrder.updateStatus(OrderStatus.CANCELLED); // Or keep pending for retry logic
            orderRepository.addOrder(newOrder); // Still add the failed order for history/tracking
            System.out.println("Order " + newOrder.getOrderId() + " failed due to payment issue.");
            return Optional.empty();
        }
    }

    public boolean fulfillOrder(String orderId) {
        Optional<Order> orderOptional = orderRepository.getOrderById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getStatus() == OrderStatus.PROCESSING && order.getPaymentStatus() == PaymentStatus.PAID) {
                // Simulate shipment to warehouse/shipping partner
                System.out.println("Sending order " + orderId + " to fulfillment center...");
                order.updateStatus(OrderStatus.SHIPPED);
                orderRepository.updateOrder(order);
                // In a real system, this would involve sending data to a shipping API
                System.out.println("Order " + orderId + " has been shipped.");
                return true;
            }
            System.out.println("Cannot fulfill order " + orderId + ". Current status: " + order.getStatus() +
                    ", Payment: " + order.getPaymentStatus());
            return false;
        }
        System.out.println("Order " + orderId + " not found.");
        return false;
    }
}
