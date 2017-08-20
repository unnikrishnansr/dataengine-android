package com.attinad.analyticsengine.core.api;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by arun.chand on 30-01-2017.
 */
public interface MiddleWareRetrofitInterface {

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST(APIConstants.BASE_PATH)
    void saveAnalyticsData(@Body JsonObject parameters, retrofit.Callback<Response> onSuccess);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST(APIConstants.BASE_PATH)
    void postIdentify(@Body HashMap parameters, retrofit.Callback<Response> onSuccess);

}
