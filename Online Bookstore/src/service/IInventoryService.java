package service;

public interface IInventoryService {
    boolean decrementStock(String bookId, int quantity);
    boolean incrementStock(String bookId, int quantity);
}
