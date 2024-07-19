package com.gabrielvicente.btgorderqueue.factory;

import com.gabrielvicente.btgorderqueue.dto.OrderCreatedEvent;
import com.gabrielvicente.btgorderqueue.dto.OrderItemEvent;

import java.math.BigDecimal;
import java.util.List;

public class OrderCreatedEventFactory {

    public static OrderCreatedEvent buildOrderEventWithOneItem() {
        OrderItemEvent orderItemEvent = buildOrderEventItem();
        return new OrderCreatedEvent(1L, 2L, List.of(orderItemEvent));
    }

    private static OrderItemEvent buildOrderEventItem() {
        return new OrderItemEvent("Notebook", 1, BigDecimal.valueOf(20.50));
    }

    public static OrderCreatedEvent buildEventWithAListOfItens() {
        List<OrderItemEvent> itens = buildOrderEventWitAListOfItens();
        return new OrderCreatedEvent(1L, 2L, itens);
    }

    private static List<OrderItemEvent> buildOrderEventWitAListOfItens() {
        OrderItemEvent item1 = new OrderItemEvent("Notebook", 1, BigDecimal.valueOf(20.50));
        OrderItemEvent item2 = new OrderItemEvent("Mouse", 1, BigDecimal.valueOf(35.25));
        return List.of(item1, item2);
    }
}
