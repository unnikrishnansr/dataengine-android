package com.attinad.analyticsengine.core.datastore.model;

/**
 * Created by arun.chand on 23-03-2017.
 */

public class DBEvents {

    private String eventType;

    private String eventName;

    private String sessionId;

    private String uniqueId;

    private String userId;

    private String presentScreenName;

    private String previousScreenName;

    private String timeStamp;

    private String timeSpent;

    private String latitude;

    private String longitude;

    private String customParams;

    private String eventDuration;

    public String getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(String eventDuration) {
        this.eventDuration = eventDuration;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPresentScreenName() {
        return presentScreenName;
    }

    public void setPresentScreenName(String presentScreenName) {
        this.presentScreenName = presentScreenName;
    }

    public String getPreviousScreenName() {
        return previousScreenName;
    }

    public void setPreviousScreenName(String previousScreenName) {
        this.previousScreenName = previousScreenName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCustomParams() {
        return customParams;
    }

    public void setCustomParams(String customParams) {
        this.customParams = customParams;
    }
}
