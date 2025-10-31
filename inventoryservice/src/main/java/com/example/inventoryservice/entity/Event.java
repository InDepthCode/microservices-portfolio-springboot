package com.example.inventoryservice.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event")
public class Event {

    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "total_capacity")
    private Long totalCapacity;

    @Column(name = "left_capacity")
    private Long leftCapacity;

    @ManyToOne // kyuki bhot sare Events -> one Venue me ho skte
    @JoinColumn(name = "venue_id") // Event table mein ek column hai — venue_id.
                                    // Ye column Venue table ke id ko point karta hai (foreign key).
    private Venue  venue;

    @Column(name="ticket_price")
    private BigDecimal ticketPrice;
}
