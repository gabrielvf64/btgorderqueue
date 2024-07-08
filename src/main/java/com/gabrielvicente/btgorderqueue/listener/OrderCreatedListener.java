package com.gabrielvicente.btgorderqueue.listener;

import com.gabrielvicente.btgorderqueue.config.RabbitMqConfig;
import com.gabrielvicente.btgorderqueue.dto.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);

    @RabbitListener(queues = RabbitMqConfig.ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreatedEvent> message) {
        logger.info("Consumed message: {}", message);
    }
}
