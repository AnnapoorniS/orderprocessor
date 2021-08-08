package com.retail.orderprocessor.services;

import com.retail.orderprocessor.models.Order;
import com.retail.orderprocessor.models.OrderStatus;
import com.retail.orderprocessor.repository.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderQueueProcessorService {

    private static final Logger logger = LogManager.getLogger(OrderQueueProcessorService.class);

    @Autowired
    OrderRepository orderRepository;

    @KafkaListener(topics = "${spring.kafka.template.default-topic}",
            groupId = "${spring.kafka.consumer.group-id}")
    public Order consume(Order order) {
        order.setStatus(OrderStatus.PROCESSED);
        logger.info("Processing order with ID: " + order.getOrderId());
        if (orderRepository.findById(order.getOrderId()).isPresent()) {
            return orderRepository.save(order);
        }
        return orderRepository.insert(order);
    }
}
