package com.builderbackend.app.enums;

public enum EventType {
    EVENT_TYPE_1(1), // Business sending message to client
    EVENT_TYPE_2(2), // Business adding new finishing selection
    EVENT_TYPE_3(3), // Business adding new task for todo list
    EVENT_TYPE_4(4), // Business adding new doc
    EVENT_TYPE_5(5), // Business adding new project update
    EVENT_TYPE_6(6), // Business adding calendar invite
    EVENT_TYPE_7(7), // Client sending message to business
    EVENT_TYPE_8(8), // Client confirmed a finishing selection
    EVENT_TYPE_9(9), // Client completed a task for todo list
    EVENT_TYPE_10(10); // Client adding a new doc

    private final int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EventType fromValue(int value) {
        for (EventType eventType : EventType.values()) {
            if (eventType.value == value) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
