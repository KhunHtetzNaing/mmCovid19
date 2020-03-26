package com.htetznaing.mmcovid19.Noti;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.htetznaing.mmcovid19.ImageViewActivity;
import com.htetznaing.mmcovid19.MyApplication;
import com.onesignal.OSInAppMessageAction;
import com.onesignal.OneSignal;

public class InAppMessageClickHandler implements OneSignal.InAppMessageClickHandler {
    // Example of an action id you could setup on the dashboard when creating the In App Message
    private MyApplication myApplication;


    public InAppMessageClickHandler(MyApplication myApplication) {
        this.myApplication = myApplication;
    }

    @Override
    public void inAppMessageClicked(OSInAppMessageAction result) {
        return;
//        if (result.clickUrl != null && result.clickUrl.length() > 1) {
//            String targetURL = result.clickUrl;
//            if (targetURL.contains("web.facebook.com")){
//                targetURL = targetURL.replace("web.facebook.com","www.facebook.com");
//            }
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse(targetURL));
//            intent.setPackage("com.facebook.katana");
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//            if (targetURL.contains("facebook.com") && targetURL.contains("/posts/")){
//                try {
//                    myApplication.startActivity(intent);
//                }catch (ActivityNotFoundException e){
//                    openDefault(targetURL);
//                }
//            }else if (targetURL.contains("i.imgur.com")){
//                myApplication.startActivity(new Intent(myApplication, ImageViewActivity.class).putExtra("url",targetURL).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK));
//            }else {
//                openDefault(targetURL);
//            }
//        }
    }

    private void openDefault(String targetURL){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(targetURL));
        myApplication.startActivity(intent);
    }
}