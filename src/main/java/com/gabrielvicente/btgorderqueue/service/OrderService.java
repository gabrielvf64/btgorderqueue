package com.gabrielvicente.btgorderqueue.service;

import com.gabrielvicente.btgorderqueue.dto.OrderCreatedEvent;
import com.gabrielvicente.btgorderqueue.dto.OrderResponse;
import com.gabrielvicente.btgorderqueue.entity.Order;
import com.gabrielvicente.btgorderqueue.entity.OrderItem;
import com.gabrielvicente.btgorderqueue.repository.OrderRepository;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(OrderCreatedEvent event) {
        Order orderEntity = new Order();
        orderEntity.setOrderId(event.orderId());
        orderEntity.setCustomerId(event.customerId());

        orderEntity.setItems(getOrderItems(event));

        orderEntity.setTotal(getTotal(event));

        orderRepository.save(orderEntity);
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        Page<Order> orders = orderRepository.findAllByCustomerId(customerId, pageRequest);
        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalSpentByCustomerId(Long customerId) {
        Aggregation aggregation = newAggregation(match(Criteria.where("customerId").is(customerId)),
                group("customerId").sum("total").as("total"));

        AggregationResults<Document> aggregationResult = mongoTemplate.aggregate(aggregation, "tb_orders", Document.class);

        return new BigDecimal(aggregationResult.getUniqueMappedResult().get("total").toString());
    }

    private List<OrderItem> getOrderItems(OrderCreatedEvent orderCreatedEvent) {
        return orderCreatedEvent.items()
                .stream()
                .map(orderItemEvent -> new OrderItem(orderItemEvent.product(), orderItemEvent.quantity(), orderItemEvent.price()))
                .toList();
    }

    private BigDecimal getTotal(OrderCreatedEvent event) {
        return event.items()
                .stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
}
