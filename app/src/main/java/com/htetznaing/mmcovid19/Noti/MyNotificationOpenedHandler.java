package com.htetznaing.mmcovid19.Noti;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.htetznaing.mmcovid19.ImageViewActivity;
import com.htetznaing.mmcovid19.MyApplication;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    MyApplication myApplication;


    public MyNotificationOpenedHandler(MyApplication myApplication) {
        this.myApplication = myApplication;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        if (result.notification.payload.launchURL.length()>1){
            String targetURL = result.notification.payload.launchURL;
            if (targetURL.contains("web.facebook.com")){
                targetURL = targetURL.replace("web.facebook.com","www.facebook.com");
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(targetURL));
            intent.setPackage("com.facebook.katana");
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            if (targetURL.contains("facebook.com") && targetURL.contains("/posts/")){
                try {
                    myApplication.startActivity(intent);
                }catch (ActivityNotFoundException e){
                    openDefault(targetURL);
                }
            }else if (targetURL.contains("i.imgur.com")){
                myApplication.startActivity(new Intent(myApplication, ImageViewActivity.class).putExtra("url",targetURL).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK));
            }else {
                openDefault(targetURL);
            }
        }
    }

    private void openDefault(String targetURL){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(targetURL));
        myApplication.startActivity(intent);
    }
}