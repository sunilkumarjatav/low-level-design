package repository;

import java.util.*;
import entities.Book;
public class BookRepository implements IBookRepository {

    private Map<String, Book> books = new HashMap<>(); // {bookId: Book_object}

    @Override
    public void addBook(Book book) {
        books.put(book.getBookId(), book);
    }

    @Override
    public Optional<Book> getBookById(String bookId) {
        return Optional.ofNullable(books.get(bookId));
    }

    @Override
    public boolean updateBook(Book book) {
        if (books.containsKey(book.getBookId())) {
            books.put(book.getBookId(), book);
            return true;
        }
        return false;
    }

    @Override
    public List<Book> searchBooks(String query, String searchBy) {
        List<Book> results = new ArrayList<>();
        String queryLower = query.toLowerCase();
        for (Book book : books.values()) {
            switch (searchBy.toLowerCase()) {
                case "title":
                    if (book.getTitle().toLowerCase().contains(queryLower)) {
                        results.add(book);
                    }
                    break;
                case "author":
                    if (book.getAuthor().stream().anyMatch(a -> a.toLowerCase().contains(queryLower))) {
                        results.add(book);
                    }
                    break;
                case "isbn":
                    if (book.getIsbn().equalsIgnoreCase(queryLower)) {
                        results.add(book);
                    }
                    break;
                case "genre":
                    if (book.getGenre().stream().anyMatch(g -> g.toLowerCase().contains(queryLower))) {
                        results.add(book);
                    }
                    break;
                default:
                    // Fallback or error for unsupported searchBy
                    break;
            }
        }
        return results;
    }
}
