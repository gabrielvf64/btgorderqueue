package com.gabrielvicente.btgorderqueue.factory;

import com.gabrielvicente.btgorderqueue.dto.OrderCreatedEvent;
import com.gabrielvicente.btgorderqueue.dto.OrderItemEvent;

import java.math.BigDecimal;
import java.util.List;

public class OrderCreatedEventFactory {

    public static OrderCreatedEvent createOrderCreatedEvent() {
        OrderItemEvent orderItemEvent = createOrderItemEvent();
        return new OrderCreatedEvent(1L, 2L, List.of(orderItemEvent));
    }

    private static OrderItemEvent createOrderItemEvent() {
        return new OrderItemEvent("Notebook", 1, BigDecimal.valueOf(20.50));
    }
}
