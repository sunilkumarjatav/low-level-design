package entities;

import builder.BookBuilder;

import java.util.*;

public class Book {
    private String bookId;
    private String title;
    private List<String> author;
    private String isbn;
    private String publisher;
    private int publicationYear;
    private List<String> genre;
    private double price;
    private int quantityAvailable;
    private String description;

    public Book(BookBuilder bookBuilder) {
        this.bookId = UUID.randomUUID().toString();
        this.title = bookBuilder.title;
        this.author = bookBuilder.author;
        this.isbn = bookBuilder.isbn;
        this.publisher = bookBuilder.publisher;
        this.publicationYear = bookBuilder.publicationYear;
        this.genre = bookBuilder.genre != null ? new ArrayList<>(bookBuilder.genre) : new ArrayList<>();
        this.price = bookBuilder.price;
        this.quantityAvailable = bookBuilder.quantityAvailable;
        this.description = bookBuilder.description;
    }

    // Getters
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public List<String> getAuthor() { return new ArrayList<>(author); }
    public String getIsbn() { return isbn; }
    public String getPublisher() { return publisher; }
    public int getPublicationYear() { return publicationYear; }
    public List<String> getGenre() { return new ArrayList<>(genre); }
    public double getPrice() { return price; }
    public int getQuantityAvailable() { return quantityAvailable; }
    public String getDescription() { return description; }

    /**
     * Adjusts quantityAvailable.
     * @param deltaQuantity The amount to change the quantity by (positive for adding, negative for deducting).
     * @return True if successful, False if insufficient stock for deduction.
     */
    public boolean updateQuantity(int deltaQuantity) {
        if (this.quantityAvailable + deltaQuantity < 0) {
            System.out.println("Error: Not enough stock for " + this.title);
            return false;
        }
        this.quantityAvailable += deltaQuantity;
        return true;
    }

    public boolean isAvailable() {
        return this.quantityAvailable > 0;
    }

    public Map<String, Object> getDetails() {
        Map<String, Object> details = new HashMap<>();
        details.put("bookId", bookId);
        details.put("title", title);
        details.put("author", author);
        details.put("isbn", isbn);
        details.put("publisher", publisher);
        details.put("publicationYear", publicationYear);
        details.put("genre", genre);
        details.put("price", price);
        details.put("quantityAvailable", quantityAvailable);
        details.put("description", description);
        return details;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId='" + bookId + '\'' +
                ", title='" + title + '\'' +
                ", author=" + author +
                ", quantityAvailable=" + quantityAvailable +
                '}';
    }
}
