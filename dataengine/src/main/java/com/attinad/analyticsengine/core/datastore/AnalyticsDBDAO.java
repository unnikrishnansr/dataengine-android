package com.attinad.analyticsengine.core.datastore;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


/*
DAO Base class. This class will take care of initializing db etc.
 */
public class AnalyticsDBDAO {

    protected SQLiteDatabase database;
    private AnalyticsDBHelper dbHelper;
    private Context mContext;

    public AnalyticsDBDAO(Context context) {
        this.mContext = context;
        dbHelper = AnalyticsDBHelper.getHelper(mContext);
        open();
    }

    public void open() throws SQLException {
        if (dbHelper == null)
            dbHelper = AnalyticsDBHelper.getHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }

}
