package com.attinad.analyticsengine.core.eventinfo;

/**
 * Created by unnikrishanansr on 14/8/17.
 */

public enum EventType {

    TRACK("Track"),
    SCREEN("ScreenView"),
    IDENTIFY("Identify"),
    SESSION("SessionStart");

    private String eventType;

    EventType(String type) {
        this.eventType=type;
    }

    public String getEventType() {
        return eventType;
    }
}
