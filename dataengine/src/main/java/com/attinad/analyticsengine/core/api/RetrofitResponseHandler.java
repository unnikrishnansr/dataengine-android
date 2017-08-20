package com.attinad.analyticsengine.core.api;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.client.Response;
import retrofit.mime.TypedInput;


public class RetrofitResponseHandler {


    public static final String DATA_TYPE_JSON_ARRAY = "arrayData";
    public static final String DATA_TYPE_STRING = "stringData";


    public static JSONObject getJSONResponse(Response response) {
        JSONObject json = convertToJSON(response);
        return json;
    }


    /**
     * Convert Retrofit response to JSON Object
     *
     * @param response
     * @return
     */
    public static JSONObject convertToJSON(Response response) {
        TypedInput body = response.getBody();
        JSONObject jsonObject = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(body.in()));
            StringBuilder out = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(newLine);
            }

            // Prints the correct String representation of body.
            System.out.println(out.toString());
            try {
                Object jsonn = new JSONTokener(out.toString()).nextValue();
                if (jsonn instanceof JSONObject) {
                    return new JSONObject(out.toString());
                } else if (jsonn instanceof JSONArray) {
                    //if you have an array, wrap it as json object. -> this is done to support the previous implementations
                    JSONObject jobj = new JSONObject();
                    JSONArray jsonArray = new JSONArray(out.toString());
                    jobj.put(DATA_TYPE_JSON_ARRAY, jsonArray);
                    return jobj;
                } else if (jsonn instanceof String) {
                    final JSONObject responseObj = new JSONObject();
                    responseObj.putOpt(DATA_TYPE_STRING, response);
                    return responseObj;
                } else if (out.toString().equalsIgnoreCase("Success")) {
                    JSONObject jobj = new JSONObject();
                    jobj.put("status", "Success");
                    return jobj;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}