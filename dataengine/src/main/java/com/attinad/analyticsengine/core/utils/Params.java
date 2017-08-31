package com.attinad.analyticsengine.core.utils;

/**
 * Created by unnikrishanansr on 16/8/17.
 */

public class Params {

    public static class BuiltInEvent {
        public static String SESSION_START = "sessionStart";
        public static String IDENTIFY = "identify";
        public static String SCREENVIEW = "screenView";
        public static String AUTO = "activityView";
    }

    public static class CustomEvent {
        public static String CONTENT_CLICK = "contentClick";
        public static String PLAY_CLICK = "playClick";
        public static String PLAY_START = "playStart";
        public static String PLAY_STATUS = "playStatus";
        public static String PLAY_PAUSE = "playPause";
        public static String PLAY_RESUME = "playResume";
        public static String PLAY_END = "playEnd";
        public static String SEARCH_CLICK = "searchClick";
        public static String SEARCH = "search";
        public static String SEARCH_RESULTS = "searchResults";
        public static String ADD_FAVOURITE = "addFavourite";
        public static String REMOVE_FAVOURITE = "removeFavourite";

    }
}
