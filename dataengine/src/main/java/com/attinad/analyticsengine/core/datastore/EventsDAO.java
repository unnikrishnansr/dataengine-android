package com.attinad.analyticsengine.core.datastore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.attinad.analyticsengine.core.datastore.model.DBEvents;

import java.util.ArrayList;

public class EventsDAO extends AnalyticsDBDAO {

    private static final String WHERE_ID_EQUALS = AnalyticsDBHelper.COLUMN_UNIQUE_ID + " =?";

    public EventsDAO(Context context) {
        super(context);
    }

    public long saveEvents(DBEvents events) {
        long result = 0;
        try {

            ContentValues values = new ContentValues();
            values.put(AnalyticsDBHelper.COLUMN_EVENT_TYPE, events.getEventType());
            values.put(AnalyticsDBHelper.COLUMN_EVENT_NAME, events.getEventName());
            values.put(AnalyticsDBHelper.COLUMN_SESSION_ID, String.valueOf(events.getSessionId()));
            values.put(AnalyticsDBHelper.COLUMN_UNIQUE_ID, events.getUniqueId());
            values.put(AnalyticsDBHelper.COLUMN_USER_ID, events.getUserId());
            values.put(AnalyticsDBHelper.COLUMN_PRESENT_SCREEN, events.getPresentScreenName());
            values.put(AnalyticsDBHelper.COLUMN_PREVIOUS_SCREEN, events.getPreviousScreenName());
            values.put(AnalyticsDBHelper.COLUMN_SCREEN_WATCH_DURATION, events.getTimeSpent());
            values.put(AnalyticsDBHelper.COLUMN_EVENT_DURATION, events.getEventDuration());
            values.put(AnalyticsDBHelper.COLUMN_TIME_STAMP, events.getTimeStamp());
            values.put(AnalyticsDBHelper.COLUMN_LATITUDE, events.getLatitude());
            values.put(AnalyticsDBHelper.COLUMN_LONGITUDE, events.getLongitude());
            values.put(AnalyticsDBHelper.COLUMN_CUSTOM_PARAM, events.getCustomParams());
            result = database.insert(AnalyticsDBHelper.EVENT_TABLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int deleteEvent(String id) {
        int result = database.delete(AnalyticsDBHelper.EVENT_TABLE, WHERE_ID_EQUALS,
                new String[]{id + ""});
        return result;
    }

    public long updateEvents(DBEvents events) {
        ContentValues values = new ContentValues();
        values.put(AnalyticsDBHelper.COLUMN_EVENT_TYPE, events.getEventType());
        values.put(AnalyticsDBHelper.COLUMN_EVENT_NAME, events.getEventName());
        values.put(AnalyticsDBHelper.COLUMN_SESSION_ID, String.valueOf(events.getSessionId()));
        values.put(AnalyticsDBHelper.COLUMN_UNIQUE_ID, events.getUniqueId());
        values.put(AnalyticsDBHelper.COLUMN_USER_ID, events.getUserId());
        values.put(AnalyticsDBHelper.COLUMN_PRESENT_SCREEN, events.getPresentScreenName());
        values.put(AnalyticsDBHelper.COLUMN_PREVIOUS_SCREEN, events.getPreviousScreenName());
        values.put(AnalyticsDBHelper.COLUMN_SCREEN_WATCH_DURATION, events.getTimeSpent());
        values.put(AnalyticsDBHelper.COLUMN_EVENT_DURATION, events.getEventDuration());
        values.put(AnalyticsDBHelper.COLUMN_TIME_STAMP, events.getTimeStamp());
        values.put(AnalyticsDBHelper.COLUMN_LATITUDE, events.getLatitude());
        values.put(AnalyticsDBHelper.COLUMN_LONGITUDE, events.getLongitude());
        values.put(AnalyticsDBHelper.COLUMN_CUSTOM_PARAM, events.getCustomParams());

        long result = database.update(AnalyticsDBHelper.EVENT_TABLE, values,
                WHERE_ID_EQUALS,
                new String[]{String.valueOf(events.getUniqueId())});
        return result;
    }

    public ArrayList<DBEvents> getEvents() {
        ArrayList<DBEvents> events = new ArrayList<DBEvents>();
        String query = "SELECT " + AnalyticsDBHelper.COLUMN_EVENT_TYPE + ","
                + AnalyticsDBHelper.COLUMN_EVENT_NAME + ","
                + AnalyticsDBHelper.COLUMN_SESSION_ID + ","
                + AnalyticsDBHelper.COLUMN_UNIQUE_ID + ","
                + AnalyticsDBHelper.COLUMN_USER_ID + ","
                + AnalyticsDBHelper.COLUMN_PRESENT_SCREEN + ","
                + AnalyticsDBHelper.COLUMN_PREVIOUS_SCREEN + ","
                + AnalyticsDBHelper.COLUMN_SCREEN_WATCH_DURATION + ","
                + AnalyticsDBHelper.COLUMN_TIME_STAMP + ","
                + AnalyticsDBHelper.COLUMN_LATITUDE + ","
                + AnalyticsDBHelper.COLUMN_LONGITUDE + ","
                + AnalyticsDBHelper.COLUMN_CUSTOM_PARAM + ","
                + AnalyticsDBHelper.COLUMN_EVENT_DURATION +
                " FROM "
                + AnalyticsDBHelper.EVENT_TABLE + " events ";

        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            DBEvents event = new DBEvents();
            event.setEventType(String.valueOf(cursor.getString(0)));
            event.setEventName(String.valueOf(cursor.getString(1)));
            event.setSessionId(String.valueOf(cursor.getString(2)));
            event.setUniqueId(String.valueOf(cursor.getString(3)));
            event.setUserId(String.valueOf(cursor.getString(4)));
            event.setPresentScreenName(String.valueOf(cursor.getString(5)));
            event.setPreviousScreenName(String.valueOf(cursor.getString(6)));
            event.setTimeSpent(String.valueOf(cursor.getString(7)));
            event.setTimeStamp(String.valueOf(cursor.getString(8)));
            event.setLatitude(String.valueOf(cursor.getString(9)));
            event.setLongitude(String.valueOf(cursor.getString(10)));
            event.setCustomParams(String.valueOf(cursor.getString(11)));
            event.setEventDuration(String.valueOf(cursor.getString(12)));
            events.add(event);
        }

        return events;
    }

}
