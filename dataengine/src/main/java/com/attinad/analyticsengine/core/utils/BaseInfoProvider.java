package com.attinad.analyticsengine.core.utils;


import android.content.Context;
import android.content.res.Configuration;
import android.provider.Settings;

import com.attinad.analyticsengine.BuildConfig;
import com.attinad.analyticsengine.core.dataprovider.LibType;
import com.attinad.analyticsengine.core.manager.SharedPreferenceManager;

import static com.attinad.analyticsengine.core.dataprovider.ContextProvider.appContext;


/**
 * Created by arun.chand on 31-01-2017.
 */
public class BaseInfoProvider {
    public static String getLibVer() {
        return BuildConfig.VERSION_NAME;
    }

    public static String getLibBuildVarient() {
        String type = SharedPreferenceManager.getInstance(appContext()).getData(Constants.LIB_VARIANT, LibType.DEBUG.getType());
        return type;
    }

    public static String getPlatform() {
        return Constants.PLATFORM;
    }

    public static String getDeviceId(Context mContext) {
        String android_id = "";
        if (mContext != null) {
            android_id = Settings.Secure.getString(mContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        return android_id;
    }

    public static String getDeviceType(Context context) {
        boolean tabletSize = Util.isTablet(context);
        if (tabletSize) {
            int uiMode = context.getResources().getConfiguration().uiMode;
            if ((uiMode & Configuration.UI_MODE_TYPE_MASK) == Configuration.UI_MODE_TYPE_TELEVISION)
                return "tv";
            else return "tablet";
        } else {
            return "mobile";
        }
    }
}
