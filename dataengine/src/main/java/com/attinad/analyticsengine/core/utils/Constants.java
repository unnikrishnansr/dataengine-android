package com.attinad.analyticsengine.core.utils;

/**
 * Created by arun.chand on 24-01-2017.
 */
public interface Constants {
    String CLASS_NAME = "class_name";
    String METHOD_NAME = "method_name";
    String PACKAGE_NAME = "package_name";
    String EVENT_TYPE = "event_type";
    String DATA_URL = "data_url";
    String METHOD_DURATION = "method_duration";
    String PLATFORM = "ANDROID";
    String LIFE_CYCLE = "life_cycle";

    // API Constants
    String SAVE_ANALYTICS_ENDPOINT = "/save";


    String SYNCED_OBJECTS = "events";
    String EVENT_ID = "eventId";

    String USER_ID = "user_id";
    String USER_MAP = "user_map";
    String TIME_SPENT = "timeSpent";
    String LOCATION = "location";
    String PREVIOUS_SCREEN = "previousScreen";
    String PRESENT_SCREEN = "sourceName";
    String DEFAULT_PARAM = "analyticEventParams";
    String DEVICE = "device";
    String NETWORK = "network";
    String EVENT_DURATION = "eventDuration";

    String USER_STATUS = "userStatus";
    String APP_ID = "app_id";

    // Identify Constants
    String LIB_VERSION = "version";
    String LIB_VARIANT = "variant";
    String SESSION_ID_IDENTIFY = "sessionId";
    String USER_ID_IDENTIFY = "userId";
    String EVENT_TYPE_IDENTIFY = "eventType";
    String EVENT_IDENTIFY = "event";
    String SESSION_START = "SessionStart";
    String TIME_STAMP_IDENTIFY = "timestamp";
    String IDENTIFY = "Identify";
    String LIB_IDENTIFY = "lib";
    String USER_IDENTIFY = "user";
    String DEVICE_IDENTIFY = "device";
    String APP_IDENTIFY = "app";
    String NETWORK_IDENTIFY = "network";
    String DATA_IDENTIFY = "data";
    String APP_SERVICE_ID = "service";
    String EVENTS_IDENTIFY = "events";
    String OS_IDENTIFY = "os";
    String APP_LANGUAGE = "appLanguage";
    String SCREEN_TRACK = "screenTrack";
    String FORCE_SCREEN_TRACK = "forceScreenTrack";

    //Shared preference keys
    String SERVER_URL = "server_url";
    String AUTO_TRACK_SCREEN_EVENTS = "auto_track";
}
