package repository;

import entities.Order;
import java.util.Optional;

public interface IOrderRepository {
    void addOrder(Order order);
    Optional<Order> getOrderById(String orderId);
    boolean updateOrder(Order order);
}
