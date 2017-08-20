package com.attinad.analyticsengine.core.datastore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AnalyticsDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "analyticsdb";
    private static final int DATABASE_VERSION = 1;

    public static final String EVENT_TABLE = "events";


    public static final String ID_COLUMN = "id";
    // Common Column
    public static final String COLUMN_EVENT_TYPE = "eventType";
    public static final String COLUMN_EVENT_NAME = "eventName";
    public static final String COLUMN_SESSION_ID = "sessionId";
    public static final String COLUMN_UNIQUE_ID = "uniqueId";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_PRESENT_SCREEN = "presentScreen";
    public static final String COLUMN_PREVIOUS_SCREEN = "previousScreen";
    public static final String COLUMN_SCREEN_WATCH_DURATION = "screenWatchDuration";
    public static final String COLUMN_TIME_STAMP= "timeStamp";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_CUSTOM_PARAM = "customParam";
    public static final String COLUMN_EVENT_DURATION = "eventDuration";
    public static final String COLUMN_SYNC_STATUS = "syncStatus";

    public static final String CREATE_EVENT_TABLE = "CREATE TABLE "
            + EVENT_TABLE + "("
            + COLUMN_UNIQUE_ID + " TEXT PRIMARY KEY, "
            + COLUMN_SESSION_ID + " TEXT, "
            + COLUMN_EVENT_TYPE + " TEXT, "
            + COLUMN_EVENT_NAME + " TEXT, "
            + COLUMN_USER_ID + " TEXT, "
            + COLUMN_LATITUDE + " TEXT, "
            + COLUMN_LONGITUDE + " TEXT, "
            + COLUMN_PRESENT_SCREEN + " TEXT, "
            + COLUMN_PREVIOUS_SCREEN + " TEXT, "
            + COLUMN_CUSTOM_PARAM + " TEXT, "
            + COLUMN_SCREEN_WATCH_DURATION + " TEXT, "
            + COLUMN_EVENT_DURATION + " TEXT, "
            + COLUMN_TIME_STAMP + " TEXT, "
            + COLUMN_SYNC_STATUS + " BOOLEAN "
            + ")";

    private static AnalyticsDBHelper instance;

    public static synchronized AnalyticsDBHelper getHelper(Context context) {
        if (instance == null)
            instance = new AnalyticsDBHelper(context);
        return instance;
    }

    private AnalyticsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
