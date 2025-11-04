package com.example.bookingservice.service;

import com.example.bookingservice.client.InventoryServiceClient;
import com.example.bookingservice.entity.Customer;
import com.example.bookingservice.event.BookingEvent;
import com.example.bookingservice.repository.CustomerRepository;
import com.example.bookingservice.request.BookingRequest;
import com.example.bookingservice.response.BookingResponse;
import com.example.bookingservice.response.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@Service
@Slf4j
public class BookingService {

    @Autowired
    private final CustomerRepository customerRepository;

    private final InventoryServiceClient inventoryServiceClient;

    private final KafkaTemplate<String , BookingEvent> kafkaTemplate;

    public BookingService(CustomerRepository customerRepository, InventoryServiceClient inventoryServiceClient, KafkaTemplate<String , BookingEvent> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public BookingResponse createBooking(final BookingRequest request){
        final Customer customer = customerRepository.findById(request.getUserId()).orElse(null);
        if(customer == null){
            throw new RuntimeException("Customer not found");
        }
        final InventoryResponse inventoryResponse = inventoryServiceClient.getInventory(request.getEventId());
        System.out.println("Invetnroy service response " + inventoryResponse);

        if (inventoryResponse.getCapacity() < request.getTicketCount()) {
            throw new RuntimeException("Not enough inventory");
        }

        // create booking event
        final BookingEvent bookingEvent = createBookingEvent(request, customer, inventoryResponse);

        // now we have a booking event so we want to send this to orderservice
        kafkaTemplate.send("booking", bookingEvent);
        log.info("Booking event created");

        return BookingResponse.builder()
                .userId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }

    public BookingEvent createBookingEvent(final BookingRequest request, final Customer customer, final InventoryResponse inventoryResponse) {
        return BookingEvent.builder()
                .eventId(request.getEventId())
                .userId(request.getUserId())
                .ticketCount(request.getTicketCount())
                .totalPrice(
                        inventoryResponse.getTicketPrice()
                                .multiply(BigDecimal.valueOf(request.getTicketCount()))
                )
                .build();
    }


}
