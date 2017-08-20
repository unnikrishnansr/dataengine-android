package com.attinad.analyticsengine.core.dataprovider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.attinad.analyticsengine.core.datamodels.AppInfo;
import com.attinad.analyticsengine.core.datamodels.DeviceInfo;
import com.attinad.analyticsengine.core.datamodels.LibInfo;
import com.attinad.analyticsengine.core.datamodels.LocationInfo;
import com.attinad.analyticsengine.core.datamodels.NetworkInfo;
import com.attinad.analyticsengine.core.datamodels.OSInfo;
import com.attinad.analyticsengine.core.datamodels.core.Data;
import com.attinad.analyticsengine.core.datamodels.core.Events;
import com.attinad.analyticsengine.core.datastore.EventsDAO;
import com.attinad.analyticsengine.core.datastore.model.DBEvents;
import com.attinad.analyticsengine.core.eventinfo.EventType;
import com.attinad.analyticsengine.core.manager.SharedPreferenceManager;
import com.attinad.analyticsengine.core.utils.BaseInfoProvider;
import com.attinad.analyticsengine.core.utils.Constants;
import com.attinad.analyticsengine.core.utils.DeviceName;
import com.attinad.analyticsengine.core.utils.LifeCycle;
import com.attinad.analyticsengine.core.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.attinad.analyticsengine.core.utils.Util.getPackageName;

/**
 * Created by unnikrishanansr on 11/8/17.
 */

public class DataCreator {
    private static DataCreator instance = null;
    private static Context mContext;

    private String GPS_PERMISSION = "android.permission.ACCESS_FINE_LOCATION";
    private String NETWORK_PERMISSION = "android.permission.ACCESS_NETWORK_STATE";

    public static DataCreator from(Context ctx) {
        mContext = ctx;
        synchronized (DataCreator.class) {
            if (instance == null) {
                instance = new DataCreator();
            }
        }
        return instance;
    }

    private DataCreator() {
    }



    public AppInfo getAppInfo() {
        AppInfo appInfo = new AppInfo();
        appInfo.setName(Util.getApplicationName(mContext));
        appInfo.setVersion(Util.getVersionName(mContext));
        appInfo.setBuild(Util.getVersionCode(mContext));
        appInfo.setNameSpace(getPackageName(mContext));
        appInfo.setVariant(Util.getAppBuildVarient(mContext, getPackageName(mContext)));
        appInfo.setLanguage(getAppLang());
        return appInfo;
    }

    public NetworkInfo getNetworkInfo() {
        NetworkInfo networkInfo = new NetworkInfo();
        networkInfo.setType(Util.getNetworkType(mContext));
        networkInfo.setCarrier(Util.getCarrierName(mContext));
        networkInfo.setConnectionSpeed(Util.getNetworkClass(mContext));
        return networkInfo;
    }

    public LocationInfo getLocationInfo() {
        double longitude = 0.0;
        double latitude = 0.0;
        Location location = getLocation();
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        return new LocationInfo(String.valueOf(latitude), String.valueOf(longitude));
    }

    public DeviceInfo getDeviceInfo() {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceId(BaseInfoProvider.getDeviceId(mContext));
        deviceInfo.setManufacture(Build.MANUFACTURER);
        deviceInfo.setLocale(Util.getCurrentLang());
        deviceInfo.setModel(Build.MODEL);
        deviceInfo.setName(DeviceName.getDeviceName());
        deviceInfo.setType(BaseInfoProvider.getDeviceType(mContext));
        Display d = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        deviceInfo.setResolution(d.getWidth() + " X " + d.getHeight());
        return deviceInfo;
    }

    public OSInfo getOSInfo() {
        return new OSInfo(BaseInfoProvider.getPlatform(), String.valueOf(Build.VERSION.RELEASE));
    }

    public LibInfo getLibInfo() {
        return new LibInfo(BaseInfoProvider.getLibVer(), BaseInfoProvider.getLibBuildVarient());
    }

    public String getAppLang() {
        String lang;
        lang = SharedPreferenceManager.getInstance(mContext).getData(Constants.APP_LANGUAGE);
        if (TextUtils.isEmpty(lang)) {
            Log.d("AOP", "Application language not set. Sending the system language. \n " +
                    "You can set it through AnalyticsTrackerHelper.setApplicationLanguage(String language);");
            lang = Locale.getDefault().getDisplayLanguage();
        }
        return lang;
    }

    @Nullable
    public Location getLocation() {
        Location location = null;
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (lm != null) {
            try {
                if (checkLocPermissionGPS()) {
                    location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            } catch (SecurityException e) {
                Log.d("AOP", "Security Exception" + e);
            }
            if (location == null) {
                try {
                    if (checkLocPermissionNetwork()) {
                        location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                } catch (SecurityException e) {
                    Log.d("AOP", "Security Exception" + e);
                }

            }
            if (location == null) {
                try {
                    if (checkLocPermissionFine()) {
                        location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    }
                } catch (SecurityException e) {
                    Log.d("AOP", "Security Exception" + e);
                }
            }
        }
        return location;
    }


    /*
    GPS Location Permission Checking
     */
    public boolean checkLocPermissionGPS() {
        int res = mContext.checkCallingOrSelfPermission(GPS_PERMISSION);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    /*
    GPS Location Permission Checking
     */
    public boolean checkLocPermissionFine() {
        int res = mContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int res2 = mContext.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        return (res == PackageManager.PERMISSION_GRANTED && res2 == PackageManager.PERMISSION_GRANTED);
    }

    /*
       Network Location Permission Checking
        */
    public boolean checkLocPermissionNetwork() {
        int res = mContext.checkCallingOrSelfPermission(NETWORK_PERMISSION);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    /*
    Build Event Object for Flushing
     */
    public JsonObject buildObject(EventsDAO eventsDAO) {
        List<DBEvents> dBEventList = eventsDAO.getEvents();
        ArrayList<Events> eventsList = new ArrayList<>();
        List<Data> aopDatas = new ArrayList<>();
        Data data = new Data();
        for (DBEvents dbEvent : dBEventList) {
            Events event = new Events();
            event.setService(getAppId());
            event.setEventType(dbEvent.getEventType());
            event.setUserId(dbEvent.getUserId());
            event.setSessionId(dbEvent.getSessionId());
            event.setEvent(dbEvent.getEventName());
            event.setEventId(dbEvent.getUniqueId());
            event.setTimestamp(dbEvent.getTimeStamp());
            event.setLib(getLibInfo());
            event.setAppInfo(getAppInfo());
            event.setDeviceInfo(getDeviceInfo());
            event.setOs(getOSInfo());
            event.setNetworkInfo(getNetworkInfo());
            event.setLocation(new com.attinad.analyticsengine.core.datamodels.core.Location(dbEvent.getLatitude(), dbEvent.getLongitude()));
            HashMap<String, Object> Subdata = new HashMap<>();
            String NULL = "null";
            if (dbEvent.getTimeSpent() != null && !dbEvent.getTimeSpent().equalsIgnoreCase(NULL))
                Subdata.put(Constants.TIME_SPENT, dbEvent.getTimeSpent());
            if (!TextUtils.isEmpty(dbEvent.getPreviousScreenName()) && dbEvent.getEventType().equals(EventType.SCREEN.getEventType()))
                Subdata.put(Constants.PREVIOUS_SCREEN, dbEvent.getPreviousScreenName());
            if (!TextUtils.isEmpty(dbEvent.getPresentScreenName()))
                Subdata.put(Constants.PRESENT_SCREEN, dbEvent.getPresentScreenName());
            if (dbEvent.getEventDuration() != null && !dbEvent.getEventDuration().equalsIgnoreCase(NULL))
                Subdata.put(Constants.EVENT_DURATION, dbEvent.getEventDuration());
            addParamHashMap(dbEvent.getCustomParams(), Subdata);
            event.setData(Subdata);
            eventsList.add(event);
        }
        data.setEvents(eventsList);
        aopDatas.add(data);
        Gson gson = new Gson();

        JsonElement element = gson.toJsonTree(aopDatas, new TypeToken<List<Data>>() {
        }.getType());
        JsonElement jsonElement;
        JsonObject jsonObj = null;
        JsonArray jsonArray = element.getAsJsonArray();
        if (jsonArray != null && jsonArray.size() > 0) {
            jsonElement = jsonArray.get(0);
            jsonObj = jsonElement.getAsJsonObject();
        }
        if (eventsList != null && !(eventsList.size() > 0)) jsonObj = null;
        eventsList.clear();
        return jsonObj;
    }

    private String getAppId() {
        return SharedPreferenceManager.getInstance(mContext).getData(Constants.APP_ID);
    }


    /**
     * String params to Hashmap conversion
     *
     * @param customParams - JSON String
     * @param subdata      - Hashmap
     */
    private void addParamHashMap(String customParams, HashMap<String, Object> subdata) {
        HashMap<String, Object> retMap = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(customParams);
            if (jsonObject != JSONObject.NULL) {
                jsonObject.remove(LifeCycle.CONTENT_NAME);
                retMap = Util.toMap(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        subdata.putAll(retMap);
    }

    public String getSessionId() {
        String value = getUserId() + Util.getCurrentTimeinMillis();
        return Util.getEncodedData(value);
    }

    public String getUserId() {
        String uId;
        String userId = SharedPreferenceManager.getInstance(mContext).getData(Constants.USER_ID);
        if (TextUtils.isEmpty(userId)) {
            //For non-logged in user it will be deviceId + package name in base64 encoded format
            uId = Util.getEncodedData(BaseInfoProvider.getDeviceId(mContext) + getPackageName(mContext));
        } else {
            uId = userId;
        }
        return uId;
    }

    /**
     * This method will retrun a combination of timestamp and Android unique Id
     *
     * @return unique Id
     */
    public String getUniqueId() {
        String time = String.valueOf(System.currentTimeMillis());
        String android_id = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return time + android_id;
    }


    /**
     * Fetch latitude and longitude
     */
    public JSONObject getLoc() {
        double longitude = 0.0;
        double latitude = 0.0;
        Location location = getLocation();
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            String LATITUDE = "latitude";
            String LONGITUDE = "longitude";
            jsonObject.put(LATITUDE, latitude);
            jsonObject.put(LONGITUDE, longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public boolean shouldTrackScreens() {
        return SharedPreferenceManager.getInstance(mContext).getBool(Constants.SCREEN_TRACK, true);
    }

}
