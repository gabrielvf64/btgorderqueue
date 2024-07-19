package com.gabrielvicente.btgorderqueue.listener;

import com.gabrielvicente.btgorderqueue.dto.OrderCreatedEvent;
import com.gabrielvicente.btgorderqueue.factory.OrderCreatedEventFactory;
import com.gabrielvicente.btgorderqueue.service.OrderService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderCreatedListenerTest {

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderCreatedListener orderCreatedListener;

    @Nested
    class Listen {

        @Test
        void shouldCallOrderServiceWithCorrectParams() {
            OrderCreatedEvent orderCreatedEvent = OrderCreatedEventFactory.createOrderCreatedEvent();
            Message<OrderCreatedEvent> message = MessageBuilder.withPayload(orderCreatedEvent).build();

            orderCreatedListener.listen(message);

            verify(orderService, times(1)).save(eq(message.getPayload()));
        }
    }

}