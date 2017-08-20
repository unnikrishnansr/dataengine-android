package com.attinad.analyticsengine.core.api;


public interface Callback<T> {

    /**
     * Execute the callback with response object.
     *
     * @param response Response object for the callback
     */
    void execute(final T response);
}