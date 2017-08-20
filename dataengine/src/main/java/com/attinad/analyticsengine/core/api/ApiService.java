package com.attinad.analyticsengine.core.api;


import com.attinad.analyticsengine.core.manager.SharedPreferenceManager;
import com.attinad.analyticsengine.core.utils.Constants;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.attinad.analyticsengine.core.dataprovider.ContextProvider.appContext;

/**
 * Created by arun.chand on 19-01-2017.
 */
public class ApiService {
    private static final String TAG = "Api Service";
    private static ApiService ourInstance = new ApiService();

    public static ApiService getInstance() {
        return ourInstance;
    }

    private ApiService() {
    }


    public void saveAnalyticData(JsonObject data, final Callback<JSONObject> onSuccess, final Callback<RetrofitError> onFailure) {

        String serverPath = SharedPreferenceManager.getInstance(appContext()).getData(Constants.SERVER_URL, APIConstants.BASE_URL);
        RetrofitClient.getMiddleware(serverPath).saveAnalyticsData(data, new retrofit.Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                onSuccess.execute(RetrofitResponseHandler.getJSONResponse(response));
            }

            @Override
            public void failure(RetrofitError error) {
                onFailure.execute(error);
            }
        });

    }


    public void identifyData(HashMap data, final Callback<JSONObject> onSuccess, final Callback<RetrofitError> onFailure) {
        String serverPath = SharedPreferenceManager.getInstance(appContext()).getData(Constants.SERVER_URL, APIConstants.BASE_URL);
        RetrofitClient.getMiddleware(serverPath).postIdentify(data, new retrofit.Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                onSuccess.execute(RetrofitResponseHandler.getJSONResponse(response));
            }

            @Override
            public void failure(RetrofitError error) {
                onFailure.execute(error);
            }
        });

    }

}
