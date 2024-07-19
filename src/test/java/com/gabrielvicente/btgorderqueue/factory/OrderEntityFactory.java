package com.gabrielvicente.btgorderqueue.factory;

import com.gabrielvicente.btgorderqueue.entity.Order;
import com.gabrielvicente.btgorderqueue.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class OrderEntityFactory {

    public static Order createOrderEntity() {
        OrderItem orderItem = createOrderItem();

        Order orderEntity = new Order();
        orderEntity.setOrderId(1L);
        orderEntity.setCustomerId(2L);
        orderEntity.setTotal(BigDecimal.valueOf(20.50));
        orderEntity.setItems(List.of(orderItem));

        return orderEntity;
    }

    private static OrderItem createOrderItem() {
        return new OrderItem("Notbook", 1, BigDecimal.valueOf(20.50));
    }
}
