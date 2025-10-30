package com.example.inventoryservice.response;


import com.example.inventoryservice.entity.Venue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventInventoryResponse {

    private String name;
    private Long capacity;
    private Venue venue;
}
