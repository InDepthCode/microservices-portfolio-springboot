package com.example.orderservice.service;


import com.example.bookingservice.event.BookingEvent;
import com.example.orderservice.client.InventoryServiceClient;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    private final InventoryServiceClient inventoryServiceClient;

    public OrderService(OrderRepository orderRepository, InventoryServiceClient inventoryServiceClient) {
        this.orderRepository = orderRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }


    @KafkaListener(topics = "booking", groupId = "order-service")
    public void orderEvent(BookingEvent bookingEvent) {
        log.info("Recieved Order Booking event: " + bookingEvent);

        // create order object for db
        Order order  = createOrder(bookingEvent);

        // this order has been saved to the repository
        orderRepository.saveAndFlush(order);

        // update inventory
        // first we have to check in inventory service is there any endpoint to uupdate inventory
        inventoryServiceClient.updateInventory(order.getEventId(), order.getTicketCount());

        log.info("Inventory updated for event: {} , ticketCount: {}", bookingEvent.getEventId(), order.getTicketCount() );


    }


    public Order createOrder(BookingEvent bookingEvent) {
        return Order.builder()
                .customerId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }
}
