package com.attinad.analyticsengine.core.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Accedo India on 05-10-2015.
 */

public class RetrofitClient {

    private static MiddleWareRetrofitInterface REST_CLIENT;

    private static String ROOT = "";

    public static void setROOT(String ROOT) {
        RetrofitClient.ROOT = ROOT;
    }


    public static MiddleWareRetrofitInterface getMiddleware(String url) {
        setROOT(url);
        return setupRestClient();
    }


    private static MiddleWareRetrofitInterface setupRestClient() {

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setEndpoint(ROOT)
                .setConverter(new GsonConverter(gson))
                .setClient(new OkClient(new OkHttpClient()))
                .setRequestInterceptor(new SessionRequestInterceptor())
                .build();

        REST_CLIENT = restAdapter.create(MiddleWareRetrofitInterface.class);
        return REST_CLIENT;
    }
}