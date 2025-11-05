package com.example.inventoryservice.controller;


import com.example.inventoryservice.response.EventInventoryResponse;
import com.example.inventoryservice.response.VenueInventoryResponse;
import com.example.inventoryservice.service.InventoryService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;


    @GetMapping("/inventory/events")
    public @ResponseBody List<EventInventoryResponse> inventoryGetAllEvents(){
        return inventoryService.getAllEvents();
    }

    @GetMapping("/inventory/location/{venueId}")
    public @ResponseBody VenueInventoryResponse inventoryResponse(@PathVariable("venueId") Long venueId){
        return inventoryService.getVenueInformation(venueId);
    }

    @GetMapping("/inventory/event/{eventId}")
    public @ResponseBody EventInventoryResponse inventoryForEvent(@PathVariable("eventId") Long eventId){
        return inventoryService.getEventInventory(eventId);
    }


    @PutMapping("/inventory/event/{eventId}/capacity/{capacity}")
    public ResponseEntity<?> updateEventCapacity(@PathVariable("eventId") Long eventId, @PathVariable("capacity") Long ticketsBooked){
        inventoryService.updateEventCapacity(eventId, ticketsBooked);
        return ResponseEntity.ok().build();
    }

}
