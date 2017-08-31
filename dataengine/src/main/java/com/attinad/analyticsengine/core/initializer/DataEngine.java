package com.attinad.analyticsengine.core.initializer;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.attinad.analyticsengine.core.appstate.AppStateListener;
import com.attinad.analyticsengine.core.crashhandler.CrashTracker;
import com.attinad.analyticsengine.core.datamodels.core.BaseEventMap;
import com.attinad.analyticsengine.core.dataprovider.DataCreator;
import com.attinad.analyticsengine.core.datastore.model.DBEvents;
import com.attinad.analyticsengine.core.eventinfo.EventType;
import com.attinad.analyticsengine.core.exception.CrashExceptionHandler;
import com.attinad.analyticsengine.core.manager.ActivityMonitor;
import com.attinad.analyticsengine.core.manager.AnalyticsManager;
import com.attinad.analyticsengine.core.manager.AppStateManager;
import com.attinad.analyticsengine.core.manager.SharedPreferenceManager;
import com.attinad.analyticsengine.core.utils.Constants;
import com.attinad.analyticsengine.core.utils.Params;
import com.attinad.analyticsengine.core.utils.Util;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by unnikrishanansr on 10/8/17.
 */

public class DataEngine {

    private static DataEngine instance;
    private static Context mContext;

    private static AnalyticsManager mAnalyticsManager;


    //For manual screenevent configuration
    private static String currentScreenName = "";
    private static String previousScreenName = "";
    private static double currentTimestamp = 0;
    private static double previousTimestamp = 0;

    //For auto screenevent configuration
    private static String autoCurrentScreenName = "";
    private static String autoPreviousScreenName = "";
    private static double autoCurrentTimestamp = 0;
    private static double autoPreviousTimestamp = 0;


    private String TAG = "DataEngine";
    private ScheduledExecutorService se;

    private ActivityMonitor am;
    private Future<?> future;

    public static DataEngine with(Context ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("Context could not be null");
        }
        mContext = ctx;
        synchronized (DataEngine.class) {
            if (instance == null) {
                instance = new DataEngine();
            }
        }
        return instance;
    }

    private DataEngine() {
        am = new ActivityMonitor(mContext);
        se = Executors.newScheduledThreadPool(1);
    }

    /**
     * To set custom analytics URL. If no value is supplied then the default builtin URL will be selected.
     *
     * @param url
     * @return instance
     */
    public DataEngine setServerURL(String url) {
        SharedPreferenceManager.getInstance(mContext).saveData(Constants.SERVER_URL, url);
        return this;
    }


    /**
     * Calling this method will enable crash reporting service.
     *
     * @return instance
     */
    public DataEngine doCaptureCrash() {
        trackCrash();
        return this;
    }


    /**
     * Sets the callback for application state monitor.
     * The callbacks defined are onAppDidEnterForeground() and onAppDidEnterBackground()
     *
     * @param stateListener
     * @return instance
     */
    public DataEngine setAppStateMonitor(AppStateListener stateListener) {
        AppStateManager.getInstance().initState(mContext, stateListener);
        return this;
    }

    /**
     * Enables the option for auto-screen capture functionality
     *
     * @param flag
     * @return instance
     */
    public DataEngine setAutoScreenCapture(boolean flag) {
        AppStateManager.getInstance().initState(mContext, appStateForAuto);
        SharedPreferenceManager.getInstance(mContext).saveBool(Constants.AUTO_TRACK_SCREEN_EVENTS, flag);
        return this;
    }

    public static DataEngine getInstance() {
        return instance;
    }

    /**
     * Initialize the library which includes starting of app state tracker
     * and setting context
     */
    public void init() {
        Log.v(TAG, "Engine Initialized");
        init(Util.getApplicationName(mContext));
    }


    /**
     * Initialize the library which includes starting of app state tracker
     * and setting context and service name
     */
    public void init(String servicename) {
        mAnalyticsManager = AnalyticsManager.getInstance(mContext, servicename);
        initializeTracker(mContext);
        mAnalyticsManager.setContext(mContext, Util.getApplicationName(mContext));
    }


    /**
     * Initialize the tracker module
     *
     * @param ctx
     */
    private static void initializeTracker(Context ctx) {
        mAnalyticsManager.setEngineRunning(true);
        mAnalyticsManager.timer();
    }


    /**
     * Start the crash tracker
     */
    public static void trackCrash() {
        if (!(Thread.getDefaultUncaughtExceptionHandler() instanceof CrashExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new CrashExceptionHandler(new CrashTracker()));
        }
    }

    /**
     * Use this method during onDestroy or onStop
     */
    public void shutdown() {
        Log.v(TAG, "Shutdown initiated");
        boolean auto = SharedPreferenceManager.getInstance(mContext).getBool(Constants.AUTO_TRACK_SCREEN_EVENTS, false);
        if (auto) {
            if (se != null) {
                if (!se.isTerminated() || !se.isShutdown()) {
                    se.shutdown();
                }
            }
        }
        AppStateManager.getInstance().stopTrackingState();
        mAnalyticsManager.setEngineRunning(false);
    }


    /**
     * Sets user id
     *
     * @param uid
     */
    public void setUserId(String uid) {
        if (!TextUtils.isEmpty(uid)) {
            SharedPreferenceManager.getInstance(mContext).saveData(Constants.USER_ID, uid);
            SharedPreferenceManager.getInstance(mContext).saveData(Constants.USER_MAP, "");
            mAnalyticsManager.createSession();
        }
    }

    /**
     * Sets user Id along with user traits or information
     *
     * @param uid
     * @param userProfile
     */
    public void setUserId(String uid, HashMap<String, Object> userProfile) {
        if (!TextUtils.isEmpty(uid)) {
            Gson gson = new Gson();
            String userMap = gson.toJson(userProfile);
            SharedPreferenceManager.getInstance(mContext).saveData(Constants.USER_ID, uid);
            SharedPreferenceManager.getInstance(mContext).saveData(Constants.USER_MAP, userMap);
            mAnalyticsManager.createSession();
        }
    }

    /**
     * Trigger identify event
     */
    public void identifyEvent() {
        Log.v(TAG, "Identify events with no parameters");
        String tempUid = DataCreator.from(mContext).getUserId();
        setUserId(tempUid);
    }

    /**
     * Trigger identify event with user id and user information map
     *
     * @param uid
     * @param map
     */
    public void identifyEvent(String uid, BaseEventMap map) {
        Log.v(TAG, "Identify events with UID and MAP parameters");
        if (map != null && !map.isEmpty()) {
            HashMap<String, Object> traits = map.getPropertyMap();
            setUserId(uid, traits);
        } else {
            setUserId(uid);
        }
    }

    /**
     * Track screen event manually
     *
     * @param screenName
     * @param map
     */
    public void screenEvent(String screenName, BaseEventMap map) {
        Log.v(TAG, "ScreenEvents");
        saveEvents(EventType.SCREEN, Params.BuiltInEvent.SCREENVIEW, screenName, map);
    }

    public void autoScreenEvent(String screenName) {
        Log.v(TAG, "Auto ScreenEvents");
        saveAutoEvents(EventType.SCREEN, Params.BuiltInEvent.AUTO, screenName, null);
    }

    public void sessionStart() {
        if (mAnalyticsManager != null) {
            mAnalyticsManager.createUserSession();
        }
    }

    /**
     * Track custom events
     *
     * @param action
     * @param sourceName
     * @param map
     */
    public void trackEvent(String action, String sourceName, BaseEventMap map) {
        Log.v(TAG, "TrackEvents");
        saveEvents(EventType.TRACK, action, sourceName, map);
    }


    protected void saveEvents(EventType type, String param1, String sourceName, BaseEventMap map) {
        Log.v(TAG, "Save Events");
        if (sourceName.isEmpty()) {
            sourceName = Util.getApplicationName(mContext);
        }

        DataCreator dataCreator = DataCreator.from(mContext);
        DBEvents events = new DBEvents();

        /**
         * The following parameters are
         * required only for screen events
         *
         * */

        if (type == EventType.SCREEN) {
            previousScreenName = currentScreenName;
            currentScreenName = sourceName;

            previousTimestamp = currentTimestamp;
            currentTimestamp = System.currentTimeMillis();

            double timeDelta = currentTimestamp - previousTimestamp;
            events.setEventDuration(Double.toString(timeDelta));
        }

        events.setUniqueId(dataCreator.getUniqueId());
        events.setUserId(dataCreator.getUserId());
        events.setPresentScreenName(sourceName);
        events.setPreviousScreenName(previousScreenName);
        events.setSessionId(dataCreator.getSessionId());
        events.setTimeStamp(String.valueOf(Util.getCurrentTimeinMillis()));
        if (dataCreator.getLoc() != null) {
            events.setLatitude(String.valueOf(dataCreator.getLoc().optDouble("latitude")));
            events.setLongitude(String.valueOf(dataCreator.getLoc().optDouble("longitude")));
        }

        events.setEventType(type.getEventType());
        JSONObject jsonObject;
        if (map != null) {
            jsonObject = new JSONObject(map.getPropertyMap());
            events.setCustomParams(String.valueOf(jsonObject));
        }
        events.setEventName(param1);
        mAnalyticsManager.newSaveEvents(events);
    }


    protected void saveAutoEvents(EventType type, String param1, String sourceName, BaseEventMap map) {
        Log.v(TAG, "Save auto events");
        if (sourceName.isEmpty()) {
            sourceName = Util.getApplicationName(mContext);
        }

        DataCreator dataCreator = DataCreator.from(mContext);
        DBEvents events = new DBEvents();

        autoPreviousScreenName = autoCurrentScreenName;
        autoCurrentScreenName = sourceName;

        autoPreviousTimestamp = autoCurrentTimestamp;
        autoCurrentTimestamp = System.currentTimeMillis();

        double timeDelta = autoCurrentTimestamp - autoPreviousTimestamp;
        events.setEventDuration(Double.toString(timeDelta));

        events.setUniqueId(dataCreator.getUniqueId());
        events.setUserId(dataCreator.getUserId());
        events.setPresentScreenName(sourceName);
        events.setPreviousScreenName(autoPreviousScreenName);
        events.setSessionId(dataCreator.getSessionId());
        events.setTimeStamp(String.valueOf(Util.getCurrentTimeinMillis()));
        if (dataCreator.getLoc() != null) {
            events.setLatitude(String.valueOf(dataCreator.getLoc().optDouble("latitude")));
            events.setLongitude(String.valueOf(dataCreator.getLoc().optDouble("longitude")));
        }

        events.setEventType(type.getEventType());
        events.setEventName(param1);
        mAnalyticsManager.newSaveEvents(events);
    }

    AppStateListener appStateForAuto = new AppStateListener() {
        @Override
        public void onAppDidEnterForeground() {
            Log.v(TAG, "Adjusting auto state- Foreground");
            boolean auto = SharedPreferenceManager.getInstance(mContext).getBool(Constants.AUTO_TRACK_SCREEN_EVENTS, false);
            if (auto) {
                if (am == null)
                    am = new ActivityMonitor(mContext);

                if (se == null)
                    se = Executors.newScheduledThreadPool(1);
                future = se.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        if (am != null)
                            am.start();
                    }
                }, 2, 6, TimeUnit.SECONDS); //Scheduled executor service starting with 2secs delay and polling with 6 seconds time interval
            }
        }

        @Override
        public void onAppDidEnterBackground() {
            Log.v(TAG, "Adjusting auto state- Background");
            boolean auto = SharedPreferenceManager.getInstance(mContext).getBool(Constants.AUTO_TRACK_SCREEN_EVENTS, false);
            if (future != null && auto) {
                if (!future.isCancelled() || !future.isDone()) {
                    future.cancel(true);
                }
            }
        }
    };
}
