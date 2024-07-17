package com.gabrielvicente.btgorderqueue.controller;

import com.gabrielvicente.btgorderqueue.dto.ApiResponse;
import com.gabrielvicente.btgorderqueue.dto.OrderResponse;
import com.gabrielvicente.btgorderqueue.dto.PaginationResponse;
import com.gabrielvicente.btgorderqueue.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable("customerId") Long customerId,
                                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        Page<OrderResponse> pageOrderResponse = orderService.findAllByCustomerId(customerId, PageRequest.of(page, pageSize));
        BigDecimal totalSpentByTheCustomer = orderService.findTotalSpentByCustomerId(customerId);

        return ResponseEntity.ok(new ApiResponse<>(
                Map.of("totalSpentByTheCustomer", totalSpentByTheCustomer),
                pageOrderResponse.getContent(),
                PaginationResponse.fromPage(pageOrderResponse)
        ));
    }
}
