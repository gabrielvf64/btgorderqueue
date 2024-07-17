package com.gabrielvicente.btgorderqueue.controller;

import com.gabrielvicente.btgorderqueue.dto.ApiResponse;
import com.gabrielvicente.btgorderqueue.dto.OrderResponse;
import com.gabrielvicente.btgorderqueue.factory.OrderResponseFactory;
import com.gabrielvicente.btgorderqueue.service.OrderService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderController orderController;

    @Nested
    class ListOrders {

        @Test
        void shouldReturnAListOfOrdersWithHttpStatusCode200() {
            var customerId = 1L;
            var page = 0;
            var pageSize = 10;

            doReturn(OrderResponseFactory.buildWithDefaultValues())
                    .when(orderService).findAllByCustomerId(anyLong(), any());

            doReturn(BigDecimal.valueOf(20.50))
                    .when(orderService).findTotalSpentByCustomerId(anyLong());

            ResponseEntity<ApiResponse<OrderResponse>> response = orderController.listOrders(customerId, page, pageSize);

            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        }
    }
}