package repository;

import java.util.List;
import java.util.Optional;
import entities.Book;

public interface IBookRepository {
    void addBook(Book book);
    Optional<Book> getBookById(String bookId);
    boolean updateBook(Book book);
    List<Book> searchBooks(String query, String searchBy);
}
