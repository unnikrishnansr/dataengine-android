package com.attinad.analyticsengine.core.datamodels;

/**
 * Created by arun.chand on 24-03-2017.
 */

public class NetworkInfo {
    private String carrier;

    private String type;

    private String connectionSpeed;

    public String getConnectionSpeed() {
        return connectionSpeed;
    }

    public void setConnectionSpeed(String connectionSpeed) {
        this.connectionSpeed = connectionSpeed;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
