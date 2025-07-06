import builder.BookBuilder;
import entities.Book;
import entities.Order;
import entities.Patron;
import processor.OrderProcessor;
import repository.*;
import service.IInventoryService;
import service.InventoryService;
import service.PaymentService;
import strategy.CreditCardPayment;
import strategy.IPaymentStrategy;
import strategy.PayPalPayment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OnlineBookstoreApp {

    private IBookRepository bookRepository;
    private IPatronRepository patronRepository;
    private IOrderRepository orderRepository;
    private IInventoryService inventoryService;
    private PaymentService paymentService;
    private OrderProcessor orderProcessor;

    public OnlineBookstoreApp() {
        this.bookRepository = new BookRepository();
        this.patronRepository = new PatronRepository();
        this.orderRepository = new OrderRepository();
        this.inventoryService = new InventoryService(this.bookRepository);
        this.paymentService = new PaymentService();
        this.orderProcessor = new OrderProcessor(
                this.bookRepository, this.orderRepository, this.inventoryService, this.paymentService
        );

        _seedData();
    }

    private void _seedData() {
        // Add some initial books
        Book book1 = new BookBuilder("The Hitchhiker's Guide to the Galaxy", "A comedic science fiction series",
                500, 10).setAuthor(List.of("Douglas Adams"))
                .setDescription("A comedic science fiction series.")
                .setGenre(List.of("Science Fiction", "Comedy"))
                .build();

        Book book2 = new BookBuilder("The Hitchhiker's Guide to the Galaxy", "A comedic science fiction series",
                1000, 20).setAuthor(List.of("Douglas Adams"))
                .setDescription("A comedic science fiction series.")
                .setGenre(List.of("Science Fiction", "Comedy"))
                .build();

        Book book3 = new BookBuilder("The Hitchhiker's Guide to the Galaxy", "A comedic science fiction series",
                3000, 25).setAuthor(List.of("Douglas Adams"))
                .setDescription("A comedic science fiction series.")
                .setGenre(List.of("Science Fiction", "Comedy"))
                .build();

        bookRepository.addBook(book1);
        bookRepository.addBook(book2);
        bookRepository.addBook(book3);

        // Add a patron
        Patron patron1 = new Patron("alice", "hashed_password_alice", "alice@example.com", "123 Main St, Anytown");
        patronRepository.addPatron(patron1);
    }

    public void runScenario() {
        System.out.println("--- Online Bookstore Scenario ---");

        // Patron logs in and adds books to cart
        Optional<Patron> patronOptional = patronRepository.getPatronByUsername("alice");
        if (patronOptional.isEmpty()) {
            System.out.println("Patron not found.");
            return;
        }
        Patron patron = patronOptional.get();

        List<Book> searchResults1 = bookRepository.searchBooks("A comedic science", "title");
        List<Book> searchResults2 = bookRepository.searchBooks("Douglas Adams", "author");

        if (searchResults1.isEmpty() || searchResults2.isEmpty()) {
            System.out.println("Required books not found in repository.");
            return;
        }

        Book book1 = searchResults1.get(0);
        Book book2 = searchResults2.get(0);

        patron.addToCart(book1.getBookId(), 2);
        patron.addToCart(book2.getBookId(), 1);
        System.out.println("Alice's cart: " + patron.getCart());

        // Place order with Credit Card
        System.out.println("\n--- Placing Order (Credit Card) ---");
        Map<String, String> paymentDetailsCc = new HashMap<>();
        paymentDetailsCc.put("cardNumber", "1111-2222-3333-4444");
        paymentDetailsCc.put("expiryDate", "12/26");
        paymentDetailsCc.put("cvv", "123");
        paymentDetailsCc.put("lastFourDigits", "4444");
        IPaymentStrategy ccStrategy = new CreditCardPayment();
        Optional<Order> orderCcOptional = orderProcessor.placeOrder(patron, patron.getShippingAddress(), paymentDetailsCc, ccStrategy);

        if (orderCcOptional.isPresent()) {
            Order orderCc = orderCcOptional.get();
            System.out.println("Order CC details: " + orderCc.getSummary());
            // Check inventory after purchase
            bookRepository.getBookById(book1.getBookId()).ifPresent(b ->
                    System.out.println("Book 1 stock after purchase: " + b.getQuantityAvailable()));
            bookRepository.getBookById(book2.getBookId()).ifPresent(b ->
                    System.out.println("Book 2 stock after purchase: " + b.getQuantityAvailable()));
            orderProcessor.fulfillOrder(orderCc.getOrderId());
        }

        // Simulate another patron trying to buy the low-stock book
        System.out.println("\n--- Simulating another patron buying low-stock book ---");
        Patron patron2 = new Patron("bob", "hashed_password_bob", "bob@example.com", "456 Oak Ave, Othertown");
        patronRepository.addPatron(patron2);

        List<Book> searchResults3 = bookRepository.searchBooks("1984", "title");
        if (searchResults3.isEmpty()) {
            System.out.println("Book '1984' not found for Bob.");
            return;
        }
        Book book3LowStock = searchResults3.get(0);

        patron2.addToCart(book3LowStock.getBookId(), 5); // Try to buy 5, but only 4 remain after Alice's purchase
        System.out.println("Bob's cart: " + patron2.getCart());
        Map<String, String> paymentDetailsPaypal = new HashMap<>();
        paymentDetailsPaypal.put("paypalEmail", "bob@example.com");
        IPaymentStrategy paypalStrategy = new PayPalPayment();
        Optional<Order> orderPaypalOptional = orderProcessor.placeOrder(patron2, patron2.getShippingAddress(), paymentDetailsPaypal, paypalStrategy);

        if (orderPaypalOptional.isPresent()) {
            Order orderPaypal = orderPaypalOptional.get();
            System.out.println("Order PayPal details: " + orderPaypal.getSummary());
        } else {
            System.out.println("Bob's order could not be placed due to insufficient stock or payment failure.");
        }

        // Demonstrate low stock alert handling (triggered by Alice's purchase of 1984)
        System.out.println("\n--- Inventory Re-stocking ---");
        // Simulate re-stocking book2 (1984)
        String book2Id = bookRepository.searchBooks("1984", "title").get(0).getBookId();
        inventoryService.incrementStock(book2Id, 10);
        bookRepository.getBookById(book2Id).ifPresent(b ->
                System.out.println("Book '1984' stock after restocking: " + b.getQuantityAvailable()));
    }

    public static void main(String[] args) {
        OnlineBookstoreApp app = new OnlineBookstoreApp();
        app.runScenario();
    }
}
