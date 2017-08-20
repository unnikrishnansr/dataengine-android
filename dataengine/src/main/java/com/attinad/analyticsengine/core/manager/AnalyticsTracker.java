package com.attinad.analyticsengine.core.manager;

import android.text.TextUtils;

import com.attinad.analyticsengine.core.dataprovider.DataCreator;
import com.attinad.analyticsengine.core.datastore.model.DBEvents;
import com.attinad.analyticsengine.core.utils.Constants;
import com.attinad.analyticsengine.core.utils.LifeCycle;
import com.attinad.analyticsengine.core.utils.Util;

import org.json.JSONObject;

import java.util.Map;

import static com.attinad.analyticsengine.core.dataprovider.ContextProvider.appContext;

/**
 * Created by arun.chand on 30-01-2017.
 */
public class AnalyticsTracker {
    private static final String SCREENVIEW = "ScreenView";
    private static String className = "";
    private static String prevClassName = "";
    private static String lastLifeCycleCalled = "";
    private double firstTime = 0;

    public void trackEvent(String message, JSONObject jsonObject, Map<String, Object> parameterMap) {
        if (jsonObject != null) {
            String clsName = jsonObject.optString(Constants.CLASS_NAME);
            String lifeCycle = jsonObject.optString(Constants.LIFE_CYCLE);
            switch (lifeCycle) {
                case LifeCycle.LIB_START:
                    lastLifeCycleCalled = LifeCycle.LIB_START;
                    firstTime = Util.getCurrentTimeinMillis();
                    break;
                case LifeCycle.LIB_STOP:
                    lastLifeCycleCalled = LifeCycle.LIB_STOP;
                    prevClassName = className;
                    className = clsName;
                    saveEvents(message, parameterMap, jsonObject.optString(Constants.METHOD_DURATION),
                            jsonObject.optBoolean(Constants.FORCE_SCREEN_TRACK));
                    break;
                case LifeCycle.LIB_RESUME:
                    if (!TextUtils.isEmpty(lastLifeCycleCalled) && lastLifeCycleCalled.equalsIgnoreCase(LifeCycle.LIB_STOP)) {
                        // Coming back to activity , which was in the back stack.
                        prevClassName = className;
                        className = clsName;
                        saveEvents(message, parameterMap, jsonObject.optString(Constants.METHOD_DURATION),
                                jsonObject.optBoolean(Constants.FORCE_SCREEN_TRACK));
                    }
                    break;
                default:
                    saveEvents(message, parameterMap, jsonObject.optString(Constants.METHOD_DURATION),
                            jsonObject.optBoolean(Constants.FORCE_SCREEN_TRACK));
                    break;
            }
        }
    }

    private void saveEvents(String message,
                            Map<String, Object> parameterMap, String methodDuration, boolean isForcedEvent) {

        DataCreator dataCreator = DataCreator.from(appContext());
        DBEvents events = new DBEvents();
        events.setUniqueId(dataCreator.getUniqueId());
        events.setUserId(dataCreator.getUserId());
        events.setPresentScreenName(className);
        events.setPreviousScreenName(prevClassName);
        events.setSessionId(dataCreator.getSessionId());
        events.setTimeStamp(String.valueOf(Util.getCurrentTimeinMillis()));
        if (dataCreator.getLoc() != null) {
            events.setLatitude(String.valueOf(dataCreator.getLoc().optDouble("latitude")));
            events.setLongitude(String.valueOf(dataCreator.getLoc().optDouble("longitude")));
        }
        String eventType = "";
        events.setEventType(eventType);
        Double duration = Util.getSpendTime(firstTime,Util.getCurrentTimeinMillis());
        double seconds;
        try {
            seconds = duration / 1000.0;
        } catch (Exception e) {
            e.printStackTrace();
            seconds = duration;
        }

        JSONObject jsonObject;
        if (parameterMap != null) {
            jsonObject = new JSONObject(parameterMap);
        } else {
            jsonObject = new JSONObject();
        }


        message = formatStructure(message, methodDuration, seconds, events, jsonObject);
        if (isValid(message)) {
            events.setEventName(message);
        }


        if (isForcedEvent || allowedToProceed(events)) {
           // analyticsManager.newSaveEvents(events);
        }
    }

    private boolean allowedToProceed(DBEvents events) {
        boolean proceed = true;
        boolean trackScreens = DataCreator.from(appContext()).shouldTrackScreens();
        if (events.getEventType().equals(SCREENVIEW) && !trackScreens) {
            proceed = false;
        }
        return proceed;
    }

    private String formatStructure(String message, String methodDuration, double seconds, DBEvents events, JSONObject jsonObject) {
        String eventType;
        if (TextUtils.isEmpty(jsonObject.optString(Constants.EVENT_TYPE))) {
            eventType = SCREENVIEW;
            message = SCREENVIEW;
            events.setTimeSpent(String.valueOf(seconds));
        } else {
            eventType = jsonObject.optString(Constants.EVENT_TYPE);
            events.setEventDuration(methodDuration);
        }
        events.setEventType(eventType);
        jsonObject.remove(Constants.EVENT_TYPE);

        events.setCustomParams(String.valueOf(jsonObject));
        return message;
    }

    private boolean isValid(String message) {
        boolean val;
        val = !(!TextUtils.isEmpty(message) && ((message.equalsIgnoreCase(LifeCycle.LIB_STOP))
                | (message.equalsIgnoreCase(LifeCycle.LIB_START)) | (message.equalsIgnoreCase(LifeCycle.LIB_RESUME))));
        return val;
    }




}
