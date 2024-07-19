package com.gabrielvicente.btgorderqueue.dto;

import com.gabrielvicente.btgorderqueue.entity.Order;
import com.gabrielvicente.btgorderqueue.factory.OrderEntityFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderResponseTest {

    @Nested
    class FromEntity {

        @Test
        void shouldReturnOrderResponseFromOrderEntity() {
            // Arrange
            Order orderEntity = OrderEntityFactory.createOrderEntity();

            // Act
            OrderResponse orderResponse = OrderResponse.fromEntity(orderEntity);

            // Assert
            assertEquals(orderEntity.getOrderId(), orderResponse.orderId());
            assertEquals(orderEntity.getCustomerId(), orderResponse.customerId());
            assertEquals(orderEntity.getTotal(), orderResponse.total());
        }
    }

}