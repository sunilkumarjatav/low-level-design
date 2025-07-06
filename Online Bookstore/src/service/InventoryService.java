package service;

import entities.Book;
import repository.IBookRepository;
import java.util.Optional;

public class InventoryService implements IInventoryService {

    private IBookRepository bookRepository;
    private final int REORDER_POINT = 10; // Example threshold

    public InventoryService(IBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean decrementStock(String bookId, int quantity) {
        Optional<Book> bookOptional = bookRepository.getBookById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (book.updateQuantity(-quantity)) {
                bookRepository.updateBook(book);
                if (book.getQuantityAvailable() < REORDER_POINT) {
                    System.out.println("!!! ALERT: Book '" + book.getTitle() + "' is low on stock (" +
                            book.getQuantityAvailable() + " left). Reorder needed!");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean incrementStock(String bookId, int quantity) {
        Optional<Book> bookOptional = bookRepository.getBookById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (book.updateQuantity(quantity)) {
                bookRepository.updateBook(book);
                System.out.println("Stock for '" + book.getTitle() + "' increased by " + quantity + ".");
                return true;
            }
        }
        return false;
    }
}
