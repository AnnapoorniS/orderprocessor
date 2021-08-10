package com.retail.orderprocessor.services;

import com.retail.orderprocessor.models.Order;
import com.retail.orderprocessor.models.OrderStatus;
import com.retail.orderprocessor.repository.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderQueueProcessorService {

    private static final Logger logger = LogManager.getLogger(OrderQueueProcessorService.class);

    @Autowired
    OrderRepository orderRepository;

    @KafkaListener(topics = "${spring.kafka.template.default-topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "orderKafkaListenerContainerFactory"
            )
    public Order consume(Order order) {
        Optional<Order> orderData = orderRepository.findById(order.getOrderId());
        if (orderData.isPresent() && orderData.get().getStatus() == OrderStatus.PLACED) {
            orderData.get().setStatus(OrderStatus.PROCESSED);
            orderRepository.save(orderData.get());
            //Logic to update inventory and place in shipment queue
            logger.info("Processing order with ID: " + order.getOrderId());
            return orderData.get();
        }
        return null;
    }
}
