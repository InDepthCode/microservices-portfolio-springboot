package com.example.inventoryservice.controller;


import com.example.inventoryservice.response.EventInventoryResponse;
import com.example.inventoryservice.response.VenueInventoryResponse;
import com.example.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
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

}
