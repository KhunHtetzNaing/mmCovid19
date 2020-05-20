package com.htetznaing.mmcovid19;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.io.UnsupportedEncodingException;

public class Constants {

    public static SharedPreferences sharedPreferences;

    public static String COVID19_ALL = "https://corona.lmao.ninja/all",
            COVID19_COUNTRIES = "https://corona.lmao.ninja/countries",
            COVID19_HISTORICAL = "https://corona.lmao.ninja/historical";

    private static String COVID19_BY_COUNTRY = "https://corona.lmao.ninja/countries/",
            COVID19_HISTORICAL_BY_COUNTRY = "https://corona.lmao.ninja/historical/";

    public static String getCovid19DataByCountry(@NonNull String country){
        return COVID19_BY_COUNTRY + country;
    }

    public static String getCovid19HistoricalByCountry(@NonNull String country){
        return COVID19_HISTORICAL_BY_COUNTRY+country;
    }

    private static String jsFunction = "aWYgKGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKCdlbWJlcjIzJykpIHsKICAgICAgICB2YXIgcCA9IGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKCdlbWJlcjIzJykuZ2V0RWxlbWVudHNCeVRhZ05hbWUoJ3AnKTsKICAgICAgICB2YXIgcmVzdWx0ID0gW107CiAgICAgICAgZm9yICh2YXIgaSA9IDA7IGkgPCBwLmxlbmd0aDsgaSsrKSB7CiAgICAgICAgICAgIHZhciB0ZXh0ID0gcC5pdGVtKGkpLnRleHRDb250ZW50OwogICAgICAgICAgICBpZiAodGV4dC5pbmRleE9mKCdsYXN0JykgIT0gLTEpIHsKICAgICAgICAgICAgICAgIHRyeSB7CiAgICAgICAgICAgICAgICAgICAgdGV4dCA9IHRleHQuc3BsaXQoJyAnKTsKICAgICAgICAgICAgICAgICAgICB0ZXh0ID0gdGV4dFt0ZXh0Lmxlbmd0aCAtIDNdICsgJyAnICsgdGV4dFt0ZXh0Lmxlbmd0aCAtIDJdICsgJyAnICsgdGV4dFt0ZXh0Lmxlbmd0aCAtIDFdCiAgICAgICAgICAgICAgICB9IGNhdGNoIHt9CiAgICAgICAgICAgICAgICByZXN1bHQucHVzaCh7CiAgICAgICAgICAgICAgICAgICAgJ3VwZGF0ZWQnOiB0ZXh0CiAgICAgICAgICAgICAgICB9KTsKICAgICAgICAgICAgICAgIGJyZWFrOwogICAgICAgICAgICB9CiAgICAgICAgICAgIHZhciB0ZW1wID0gdGV4dC5zcGxpdCgnICcpLAogICAgICAgICAgICAgICAgY2FzZXMgPSB0ZW1wWzBdLAogICAgICAgICAgICAgICAgY291bnRzID0gdGVtcFt0ZW1wLmxlbmd0aCAtIDFdOwogICAgICAgICAgICBpZiAoY2FzZXMuaW5kZXhPZignwqAnKSAhPSAtMSkgewogICAgICAgICAgICAgICAgY2FzZXMgPSBjYXNlcy5zcGxpdCgnwqAnKVswXTsKICAgICAgICAgICAgfQogICAgICAgICAgICBpZiAoY2FzZXMpIHsKICAgICAgICAgICAgICAgIHJlc3VsdC5wdXNoKHsKICAgICAgICAgICAgICAgICAgICAnY2FzZXMnOiBjYXNlcywKICAgICAgICAgICAgICAgICAgICAnY291bnRzJzogY291bnRzCiAgICAgICAgICAgICAgICB9KTsKICAgICAgICAgICAgfQogICAgICAgIH0KICAgICAgICBIdGV0ek5haW5nLmZ1Y2soSlNPTi5zdHJpbmdpZnkocmVzdWx0KSk7CiAgICB9",
    newsFeed = "PHJzc2FwcC13YWxsIGlkPSIwbUxFQ0ZPd1NHOVpEOW41Ij48L3Jzc2FwcC13YWxsPjxzY3JpcHQgc3JjPSJodHRwczovL3dpZGdldC5yc3MuYXBwL3YxL3dhbGwuanMiIHR5cGU9InRleHQvamF2YXNjcmlwdCIgYXN5bmM+PC9zY3JpcHQ+",
    newsFb = "199295433433103",
    newsWeb = "http://mohs.gov.mm/Main/content/publication/2019-ncov",
    emergency = "";

    public static String getEmergency() {
        String temp = sharedPreferences.getString("emergency",null);
        return temp!=null ? temp : emergency;
    }

    public static void setEmergency(String emergency) {
        Constants.emergency = emergency;
        sharedPreferences.edit().putString("emergency",emergency).apply();
    }

    public static String getNewsFeed() {
        String temp = sharedPreferences.getString("newsFeed",null);
        return temp!=null ? decodeBase64(temp) : decodeBase64(newsFeed);
    }

    public static void setNewsFeed(String newsFeed) {
        Constants.newsFeed = newsFeed;
        sharedPreferences.edit().putString("newsFeed",newsFeed).apply();
    }

    public static String getNewsFb() {
        String temp = sharedPreferences.getString("newsFb",null);
        return temp!=null ? temp : newsFb;
    }

    public static void setNewsFb(String newsFb) {
        Constants.newsFb = newsFb;
        sharedPreferences.edit().putString("newsFb",newsFb).apply();
    }

    public static String getNewsWeb() {
        String temp = sharedPreferences.getString("newsWeb",null);
        return temp!=null ? temp : newsWeb;
    }

    public static void setNewsWeb(String newsWeb) {
        Constants.newsWeb = newsWeb;
        sharedPreferences.edit().putString("newsWeb",newsWeb).apply();
    }

    public static void setJsFunction(String jsFunction) {
        Constants.jsFunction = jsFunction;
        sharedPreferences.edit().putString("js_mohs",jsFunction).apply();
    }

    public static String decodeBase64(String coded){
        try {
            return new String(Base64.decode(coded.getBytes("UTF-8"), Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getJsFunction() {
        String temp = sharedPreferences.getString("js_mohs",null);
        return temp!=null ? decodeBase64(temp) : decodeBase64(jsFunction);
    }


    public static String cleanNumber(String num){
        return num.replaceAll("\\D","");
    }

    public static String toMmNum(String num){
        num = num.replaceAll("\\D","");
        String out = "";
        for (String a:num.split("")){
            out+=replace(a);
        }
        return out;
    }

    public static void openMOHS_MOBILE(Context c){
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("https://mohs.gov.mm/Main/content/publication/2019-ncov"));
        c.startActivity(intent);
    }

    private static String replace(String number){
        switch (number){
            case "1": return "၁";
            case "2": return "၂";
            case "3": return "၃";
            case "4": return "၄";
            case "5": return "၅";
            case "6": return "၆";
            case "7": return "၇";
            case "8": return "၈";
            case "9": return "၉";
            case "0": return "၀";
        }
        return number;
    }

    public static String notNullString(String a){
        if (a==null){return a;}
        a = a.trim();
        if (a.isEmpty()){
            return "0";
        }
        return a;
    }

    public static void showFeedback(Context context){
        final SpannableString s = new SpannableString("အရေးပေါ်ဆက်သွယ်နိုင်မည့်ဖုန်းနံပါတ်များနှင့်\n" +
                "အခြားအက်ပလီကေးရှင်းနှင့်ပတ်သက်သော\n" +
                "အကြံပြုချက်များကို အီးမေးလ်မှတဆင့်\n" +
                "ဆက်သွယ်ပေးပို့ အကြံပြုနိုင်ပါသည်။\n" +
                "\n" +
                "အီးမေးလ် : khunhtetznaing@gmail.com");
        Linkify.addLinks(s, Linkify.EMAIL_ADDRESSES);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_title,null);
        TextView title = view.findViewById(R.id.title);
        title.setText(context.getString(R.string.welcome));
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCustomTitle(view)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(s)
                .setPositiveButton(context.getString(R.string.ok),null);
        AlertDialog dialog = builder.create();
        dialog.show();
        try {
            TextView textView = dialog.findViewById(android.R.id.message);
            if (textView!=null){
                textView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }catch (Exception e){e.printStackTrace();}
    }
}
