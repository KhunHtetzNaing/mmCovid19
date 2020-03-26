package com.htetznaing.mmcovid19.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternet {
    Context context;
    public CheckInternet(Context context){
        this.context = context;
    }

    private final boolean isInternetOn() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public boolean checkInternet(){
        if (isInternetOn()){
            return true;
        }else{
            return false;
        }
    }
}
