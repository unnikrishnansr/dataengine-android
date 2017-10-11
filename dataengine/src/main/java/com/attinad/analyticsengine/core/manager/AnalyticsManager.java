package com.attinad.analyticsengine.core.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.attinad.analyticsengine.core.api.ApiService;
import com.attinad.analyticsengine.core.api.Callback;
import com.attinad.analyticsengine.core.dataprovider.DataCreator;
import com.attinad.analyticsengine.core.datastore.EventsDAO;
import com.attinad.analyticsengine.core.datastore.model.DBEvents;
import com.attinad.analyticsengine.core.utils.Constants;
import com.attinad.analyticsengine.core.utils.Params;
import com.attinad.analyticsengine.core.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.RetrofitError;


/**
 * Created by arun.chand on 23-01-2017.
 * <p>
 * Take care of the analytic events flush management
 */
public class AnalyticsManager {

    private static AnalyticsManager instance = new AnalyticsManager();
    private EventsDAO eventsDAO;
    private boolean enableEngine = false;
    private static String userId;
    private static String appId;
    private static String sessionId;

    public static String BASE_URL = "http://111.93.108.38:8089/Analytics/";

    private DataCreator mDataCreator;

    private static Context mContext;
    private static String mAppId;


    public static AnalyticsManager getInstance(Context context, String appId) {
        if (context == null) {
            throw new IllegalArgumentException("Context is NULL");
        }
        if (appId == null) {
            throw new IllegalArgumentException("AppId is NULL");
        }
        mContext = context;
        mAppId = appId;

        synchronized (AnalyticsManager.class) {
            if (instance == null) {
                instance = new AnalyticsManager();
            }
        }
        return instance;
    }

    private AnalyticsManager() {
        mDataCreator = DataCreator.from(mContext);
    }

    /*
     Setting context
     */
    public void setContext(Context context) {
        this.mContext = context;
        eventsDAO = new EventsDAO(context.getApplicationContext());
    }

    /*
     Setting context with appId
     */
    public void setContext(Context context, String appId) {
        this.mContext = context;
        eventsDAO = new EventsDAO(context.getApplicationContext());
        setAppId(appId);
    }

    private void setAppId(String appId) {
        SharedPreferenceManager.getInstance(mContext).saveData(Constants.APP_ID, appId);
        AnalyticsManager.appId = appId;
    }

    private String getAppId() {
        return appId;
    }

    /**
     * Save events to db.
     *
     * @param events - Events - analytic Single Event
     */
    public void newSaveEvents(final DBEvents events) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return eventsDAO.saveEvents(events);
            }

            @Override
            protected void onPostExecute(Long result) {
                super.onPostExecute(result);
            }
        }.execute();
    }


    /**
     * Build event object for identify.
     *
     * @param onBuild
     */
    private void buildBaseObject(Callback<HashMap> onBuild) {
        HashMap mainData = new HashMap();
        ArrayList<HashMap> jsonList = new ArrayList<>();
        try {

            //Session object
            HashMap sessionObj = new HashMap();
            sessionObj.put(Constants.APP_SERVICE_ID, getAppId());
            sessionObj.put(Constants.SESSION_ID_IDENTIFY, getSessionId());
            sessionObj.put(Constants.USER_ID_IDENTIFY, mDataCreator.getUserId());
            sessionObj.put(Constants.EVENT_TYPE_IDENTIFY, Constants.SESSION_START);
            sessionObj.put(Constants.EVENT_IDENTIFY, Params.BuiltInEvent.SESSION_START);
            sessionObj.put(Constants.TIME_STAMP_IDENTIFY, Util.getCurrentTimeinMillis());
            sessionObj.put(Constants.LIB_IDENTIFY, mDataCreator.getLibInfo());
            sessionObj.put(Constants.DEVICE_IDENTIFY, mDataCreator.getDeviceInfo());
            sessionObj.put(Constants.OS_IDENTIFY, mDataCreator.getOSInfo());
            sessionObj.put(Constants.APP_IDENTIFY, mDataCreator.getAppInfo());
            sessionObj.put(Constants.NETWORK_IDENTIFY, mDataCreator.getNetworkInfo());
            sessionObj.put(Constants.LOCATION, mDataCreator.getLocationInfo());

            //Identify object
            HashMap identify = new HashMap();
            identify.put(Constants.APP_SERVICE_ID, getAppId());
            identify.put(Constants.EVENT_IDENTIFY, Params.BuiltInEvent.IDENTIFY);
            identify.put(Constants.USER_ID_IDENTIFY, mDataCreator.getUserId());
            identify.put(Constants.EVENT_TYPE_IDENTIFY, Constants.IDENTIFY);
            identify.put(Constants.SESSION_ID_IDENTIFY, getSessionId());
            identify.put(Constants.TIME_STAMP_IDENTIFY, String.valueOf(Util.getCurrentTimeinMillis()));
            identify.put(Constants.LIB_IDENTIFY, mDataCreator.getLibInfo());
            identify.put(Constants.DEVICE_IDENTIFY, mDataCreator.getDeviceInfo());
            identify.put(Constants.OS_IDENTIFY, mDataCreator.getOSInfo());
            identify.put(Constants.APP_IDENTIFY, mDataCreator.getAppInfo());
            identify.put(Constants.NETWORK_IDENTIFY, mDataCreator.getNetworkInfo());
            identify.put(Constants.LOCATION, mDataCreator.getLocationInfo());
            identify.put(Constants.DATA_IDENTIFY, getUserHashMap());


            jsonList.add(identify);
            jsonList.add(sessionObj);

            mainData.put(Constants.EVENTS_IDENTIFY, jsonList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onBuild.execute(mainData);
    }


    private void buildSessionObject(Callback<HashMap> onBuild) {
        HashMap mainData = new HashMap();
        ArrayList<HashMap> jsonList = new ArrayList<>();
        try {

            //Session object
            HashMap sessionObj = new HashMap();
            sessionObj.put(Constants.APP_SERVICE_ID, getAppId());
            sessionObj.put(Constants.SESSION_ID_IDENTIFY, getSessionId());
            sessionObj.put(Constants.USER_ID_IDENTIFY, mDataCreator.getUserId());
            sessionObj.put(Constants.EVENT_TYPE_IDENTIFY, Constants.SESSION_START);
            sessionObj.put(Constants.EVENT_IDENTIFY, Params.BuiltInEvent.SESSION_START);
            sessionObj.put(Constants.TIME_STAMP_IDENTIFY, Util.getCurrentTimeinMillis());
            sessionObj.put(Constants.LIB_IDENTIFY, mDataCreator.getLibInfo());
            sessionObj.put(Constants.DEVICE_IDENTIFY, mDataCreator.getDeviceInfo());
            sessionObj.put(Constants.OS_IDENTIFY, mDataCreator.getOSInfo());
            sessionObj.put(Constants.APP_IDENTIFY, mDataCreator.getAppInfo());
            sessionObj.put(Constants.NETWORK_IDENTIFY, mDataCreator.getNetworkInfo());
            sessionObj.put(Constants.LOCATION, mDataCreator.getLocationInfo());

            jsonList.add(sessionObj);
            mainData.put(Constants.EVENTS_IDENTIFY, jsonList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        onBuild.execute(mainData);
    }


    /*
    Post data to middleware
     */
    private void postData() {
        final JsonObject jsonObject = mDataCreator.buildObject(eventsDAO);
        if (jsonObject != null) {
            ApiService.getInstance().saveAnalyticData(jsonObject, new Callback<JSONObject>() {
                @Override
                public void execute(JSONObject response) {
                    JSONArray jsonArray = response.optJSONArray(Constants.SYNCED_OBJECTS);
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                String id = jObj.optString(Constants.EVENT_ID);
                                deleteData(id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }, new Callback<RetrofitError>() {
                @Override
                public void execute(RetrofitError response) {
                    Log.d("DB", "ERROR while sending to middleware " + response.getMessage());
                }
            });
        }
    }


    /*
   Post Identify data to middleware
    */
    void postIdentify() {
        buildBaseObject(new Callback<HashMap>() {
            @Override
            public void execute(HashMap response) {
                if (response != null) {
                    ApiService.getInstance().identifyData(response, new Callback<JSONObject>() {
                        @Override
                        public void execute(JSONObject response) {
                        }
                    }, new Callback<RetrofitError>() {
                        @Override
                        public void execute(RetrofitError response) {
                            Log.d("DB", "ERROR while sending to middleware " + response.getMessage());
                        }
                    });
                }
            }
        });
    }


    void postSession() {
        buildSessionObject(new Callback<HashMap>() {
            @Override
            public void execute(HashMap response) {
                if (response != null) {
                    ApiService.getInstance().identifyData(response, new Callback<JSONObject>() {
                        @Override
                        public void execute(JSONObject response) {
                        }
                    }, new Callback<RetrofitError>() {
                        @Override
                        public void execute(RetrofitError response) {
                            Log.d("DB", "ERROR while sending to middleware " + response.getMessage());
                        }
                    });
                }
            }
        });
    }

    /**
     * Delete the data from local db, once sync is success
     *
     * @param id screenViewId
     */
    private void deleteData(final String id) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return eventsDAO.deleteEvent(id);
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
            }
        }.execute();
    }

    /**
     * Flush Mechanism, timer set to flush the data to middleware
     */
    public void timer() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if (Util.isDataAvailable(mContext) && enableEngine) {
                                postData();
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 10000); //execute in every 10000 ms
    }

    public void setEngineRunning(boolean enableEngine) {
        this.enableEngine = enableEngine;
    }

    private HashMap getUserHashMap() {
        Gson gson = new Gson();
        String userMap = SharedPreferenceManager.getInstance(mContext).getData(Constants.USER_MAP);
        boolean userLoginStatus = SharedPreferenceManager.getInstance(mContext).getBool(Constants.USER_LOGIN_STATUS, false);
        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        HashMap<String, Object> userHashMap;
        if (!TextUtils.isEmpty(userMap)) {
            userHashMap = gson.fromJson(userMap, type);
        } else {
            userHashMap = new HashMap<>();
        }
        int userStatus = (!userLoginStatus) ? 0 : 1;
        userHashMap.put(Constants.USER_STATUS, userStatus);
        return userHashMap;
    }


    /**
     * Clear the user profile from shared preference.
     */
    public void resetUser() {
        userId = "";
        SharedPreferenceManager.getInstance(mContext).saveData(Constants.USER_ID, "");
        SharedPreferenceManager.getInstance(mContext).saveData(Constants.USER_MAP, "");
        createSession();
    }

    public String getSessionId() {
        if (sessionId == null) {
            sessionId = mDataCreator.getSessionId();
        }
        return sessionId;
    }

    /*
    Create session (Encoded Combination of device id + appID + timestamp)
     */
    public void createSession() {
        //User session will be created by appending timestamp with userId and it will be in base64 encoded format
        sessionId = mDataCreator.getSessionId();
        postIdentify();
    }

    public void createUserSession() {
        sessionId = mDataCreator.getSessionId();
        postSession();
    }


    public void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    public void setLanguage(String language) {
        SharedPreferenceManager.getInstance(mContext).saveData(Constants.APP_LANGUAGE, language);
    }


    public void trackScreens(boolean isDisable) {
        SharedPreferenceManager.getInstance(mContext).saveBool(Constants.SCREEN_TRACK, !isDisable);
    }

}
