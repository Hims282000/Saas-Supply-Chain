package com.supplychain.shipment_service.Model;

public enum ShipmentPriority {
    STANDARD,
    PRIORITY,
    EXPRESS,
    DELIVERED

    private final String displayName;
    private final int slaDays;


    private ShipmentPriority(String displayName, int slaDays) {
        this.displayName = displayName;
        this.slaDays = slaDays;
    }


    public String getDisplayName() {
        return displayName;
    }


    public int getSlaDays() {
        return slaDays;
    }

    

    

    
}
