package com.gabrielvicente.btgorderqueue.repository;

import com.gabrielvicente.btgorderqueue.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, Long> {
}
