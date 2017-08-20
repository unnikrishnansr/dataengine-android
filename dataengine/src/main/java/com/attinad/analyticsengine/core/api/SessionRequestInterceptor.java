package com.attinad.analyticsengine.core.api;

import retrofit.RequestInterceptor;


public class SessionRequestInterceptor implements RequestInterceptor {
    private static final String TAG = SessionRequestInterceptor.class.getSimpleName();
    String CONTENT_TYPE_JSON = "application/json";
    String KEY_CONTENT_TYPE = "Content-Type";

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader(KEY_CONTENT_TYPE, CONTENT_TYPE_JSON);//you can add header here if you need in your api
    }
}