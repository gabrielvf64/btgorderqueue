package com.gabrielvicente.btgorderqueue.factory;

import com.gabrielvicente.btgorderqueue.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponseFactory {

    public static Page<OrderResponse> buildWithDefaultValues() {
        OrderResponse orderResponse = new OrderResponse(1L, 2L, BigDecimal.valueOf(20.50));
        return new PageImpl<>(List.of(orderResponse));
    }
}
