package com.gabrielvicente.btgorderqueue.dto;

import java.util.List;

public record OrderCreatedEvent(Long orderId,
                                Long customerId,
                                List<OrderItemEvent> items) {
}
