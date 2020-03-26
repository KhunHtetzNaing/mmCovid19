package com.htetznaing.mmcovid19;

import android.app.Application;

import com.htetznaing.mmcovid19.Noti.InAppMessageClickHandler;
import com.htetznaing.mmcovid19.Noti.MyNotificationOpenedHandler;
import com.onesignal.OneSignal;
import com.onesignal.OneSignalDbHelper;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Constants.sharedPreferences = getSharedPreferences("convid19_mm",MODE_PRIVATE);

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("Pyidaungsu-2.5.3_Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler(this))
                .setInAppMessageClickHandler(new InAppMessageClickHandler(this))
                .init();
    }
}
