package com.attinad.analyticsengine.core.dataprovider;

/**
 * Provide brief class description
 *
 * @author Unnikrishnan SR (unnikrishnan.sr@attinadsoftware.com) on 13-09-2017.
 * @since Media Proto
 * Copyright (c) 2017 Attinad Software Pvt Ltd. All rights reserved.
 */
public enum LibType {

    PROD("prod"),
    DEBUG("debug");

    String mType;

    LibType(String type) {
        this.mType = type;
    }

    public String getType() {
        return mType;
    }
}
