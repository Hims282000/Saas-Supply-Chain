package com.supplychain.shipment_service.Model;

public enum ShipmentStatus {
    CREATED,
    PICKED_UP,
    IN_TRANSIT,
    AT_HUB,
    OUT_FOR_DELIVERY,
    DELIVERED,
    DELAYED,
    CANCELLED,
    EXCEPTION,
    RETURNED

    private final String description;

    ShipmentStatus(String description){
        this.description=description;

    }
    public String getDescription(){
        return description;
    }

    
}
