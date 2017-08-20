package com.attinad.analyticsengine.core.datamodels.core;


import com.attinad.analyticsengine.core.datamodels.AppInfo;
import com.attinad.analyticsengine.core.datamodels.DeviceInfo;
import com.attinad.analyticsengine.core.datamodels.LibInfo;
import com.attinad.analyticsengine.core.datamodels.NetworkInfo;
import com.attinad.analyticsengine.core.datamodels.OSInfo;

import java.util.HashMap;

/**
 * Created by arun.chand on 23-03-2017.
 */

public class Events {

    private String service;
    private String eventType;
    private String event;
    private String userId;
    private String eventId;
    private String sessionId;
    private String timestamp;
    private HashMap data;
    private LibInfo lib;
    private AppInfo app;
    private OSInfo os;
    private DeviceInfo device;
    private NetworkInfo network;
    private Location location;

    public HashMap getData() {
        return data;
    }

    public void setData(HashMap data) {
        this.data = data;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getEvent() {
        return event;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setLib(LibInfo lib) {
        this.lib = lib;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.app = appInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.device = deviceInfo;
    }

    public void setNetworkInfo(NetworkInfo networkInfo) {
        this.network = networkInfo;
    }

    public void setOs(OSInfo osInfo) {
        this.os = osInfo;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setService(String service) {
        this.service = service;
    }
}
