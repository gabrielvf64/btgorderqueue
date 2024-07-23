package com.gabrielvicente.btgorderqueue.service;

import com.gabrielvicente.btgorderqueue.dto.OrderCreatedEvent;
import com.gabrielvicente.btgorderqueue.dto.OrderResponse;
import com.gabrielvicente.btgorderqueue.entity.Order;
import com.gabrielvicente.btgorderqueue.factory.OrderCreatedEventFactory;
import com.gabrielvicente.btgorderqueue.factory.OrderEntityFactory;
import com.gabrielvicente.btgorderqueue.repository.OrderRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    MongoTemplate mongoTemplate;

    @InjectMocks
    OrderService orderService;

    @Captor
    ArgumentCaptor<Order> orderEntityArgumentCaptor;

    @Nested
    class save {

        @Test
        void shouldCallRepositorySave() {
            OrderCreatedEvent event = OrderCreatedEventFactory.buildOrderEventWithOneItem();

            orderService.save(event);

            verify(orderRepository, times(1)).save(any());
        }

        @Test
        void shouldConvertEventToOrderEntity() {
            OrderCreatedEvent event = OrderCreatedEventFactory.buildOrderEventWithOneItem();

            orderService.save(event);

            verify(orderRepository, times(1)).save(orderEntityArgumentCaptor.capture());

            Order orderEntity = orderEntityArgumentCaptor.getValue();

            assertEquals(event.items().getFirst().product(), orderEntity.getItems().getFirst().getProduct());
            assertEquals(event.items().getFirst().quantity(), orderEntity.getItems().getFirst().getQuantity());
            assertEquals(event.items().getFirst().price(), orderEntity.getItems().getFirst().getPrice());

            assertEquals(event.orderId(), orderEntity.getOrderId());
            assertEquals(event.customerId(), orderEntity.getCustomerId());

            assertNotNull(orderEntity.getTotal());
        }

        @Test
        void shouldCalculateTotal() {
            OrderCreatedEvent event = OrderCreatedEventFactory.buildEventWithAListOfItens();
            BigDecimal item1Total = event.items().getFirst().price().multiply(BigDecimal.valueOf(event.items().getFirst().quantity()));
            BigDecimal item2Total = event.items().getLast().price().multiply(BigDecimal.valueOf(event.items().getLast().quantity()));
            BigDecimal orderTotal = item1Total.add(item2Total);

            orderService.save(event);

            verify(orderRepository, times(1)).save(orderEntityArgumentCaptor.capture());

            Order orderEntity = orderEntityArgumentCaptor.getValue();

            assertNotNull(orderEntity.getTotal());
            assertEquals(orderTotal, orderEntity.getTotal());
        }
    }

    @Nested
    class findAllByCustomerId {

        @Test
        void shouldCallRepository() {
            Long customerId = 1L;
            PageRequest pageRequest = PageRequest.of(0, 10);

            doReturn(OrderEntityFactory.createOrderEntityPage())
                    .when(orderRepository)
                    .findAllByCustomerId(eq(customerId), eq(pageRequest));

            orderService.findAllByCustomerId(customerId, pageRequest);

            verify(orderRepository, times(1))
                    .findAllByCustomerId(eq(customerId), eq(pageRequest));
        }

        @Test
        void shouldMapResponse() {
            Long customerId = 1L;
            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Order> orderPage = OrderEntityFactory.createOrderEntityPage();

            doReturn(orderPage)
                    .when(orderRepository)
                    .findAllByCustomerId(anyLong(), any());

            Page<OrderResponse> response = orderService.findAllByCustomerId(customerId, pageRequest);

            assertEquals(orderPage.getTotalPages(), response.getTotalPages());
            assertEquals(orderPage.getTotalElements(), response.getTotalElements());
            assertEquals(orderPage.getSize(), response.getSize());
            assertEquals(orderPage.getNumber(), response.getNumber());

            assertEquals(orderPage.getContent().getFirst().getOrderId(), response.getContent().getFirst().orderId());
            assertEquals(orderPage.getContent().getFirst().getCustomerId(), response.getContent().getFirst().customerId());
            assertEquals(orderPage.getContent().getFirst().getTotal(), response.getContent().getFirst().total());
        }
    }

}