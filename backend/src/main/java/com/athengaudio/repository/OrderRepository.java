package com.athengaudio.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.athengaudio.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);
    List<Order> findByStatus(Order.OrderStatus status);
    List<Order> findByUserIdAndStatus(String userId, Order.OrderStatus status);
}