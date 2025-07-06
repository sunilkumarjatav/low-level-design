package builder;

import entities.Book;
import java.util.List;

public class BookBuilder {
    public String bookId;
    public String title;
    public List<String> author;
    public String isbn;
    public String publisher;
    public int publicationYear;
    public List<String> genre;
    public double price;
    public int quantityAvailable;
    public String description;

    public BookBuilder(String bookId, String title, double price, int quantityAvailable) {
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
    }

    public BookBuilder setAuthor(List<String> author) {
        this.author = author;
        return this;
    }

    public BookBuilder setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public BookBuilder setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public BookBuilder setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
        return this;
    }

    public BookBuilder setGenre(List<String> genre) {
        this.genre = genre;
        return this;
    }

    public BookBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public Book build() {
        return new Book(this);
    }
}
