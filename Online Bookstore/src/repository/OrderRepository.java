package repository;

import entities.Order;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OrderRepository implements IOrderRepository {

    private Map<String, Order> orders = new HashMap<>(); // {orderId: Order_object}

    @Override
    public void addOrder(Order order) {
        orders.put(order.getOrderId(), order);
    }

    @Override
    public Optional<Order> getOrderById(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    @Override
    public boolean updateOrder(Order order) {
        if (orders.containsKey(order.getOrderId())) {
            orders.put(order.getOrderId(), order);
            return true;
        }
        return false;
    }
}
