package com.retail.orderprocessor.repository;

import com.retail.orderprocessor.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
