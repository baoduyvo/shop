package com.example.shop.infrastructure.common.contants;

public enum EventType {
    COMMAND("COMMAND"),
    EVENT("EVENT");

    private final String type;

    private EventType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
