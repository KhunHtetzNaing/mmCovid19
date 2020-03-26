package com.htetznaing.mmcovid19;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.htetznaing.mmcovid19.Model.UpdateModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppUpdater {
    private static Context activity;
    private static AlertDialog dialog;
    public static void init(Context context){
        activity = context;
    }

    private static String query(String key,JSONObject object){
        if (object.has(key)){
            try {
                return object.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void letCheck(String response){
        if (activity!=null) {
                UpdateModel data = new UpdateModel();
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        data.setVersionName(query("versionName", object));
                        data.setTitle(query("title", object));
                        data.setMessage(query("message", object));
                        data.setDownload(query("download", object));
                        data.setPlaystore(query("playstore", object));
                        data.setUninstall(object.getBoolean("uninstall"));
                        data.setVersionCode(object.getInt("versionCode"));
                        data.setForce(object.getBoolean("force"));

                        if (object.has("what")) {
                            JSONObject what = object.getJSONObject("what");
                            data.setAll(what.getBoolean("all"));
                            JSONArray versions = what.getJSONArray("version");

                            int[] v_int = new int[versions.length()];
                            for (int i = 0; i < versions.length(); i++) {
                                v_int[i] = versions.getInt(i);
                            }
                            data.setVersions(v_int);

                            JSONArray models = what.getJSONArray("model");
                            String[] models_string = new String[models.length()];
                            for (int i = 0; i < models.length(); i++) {
                                models_string[i] = models.getString(i);
                            }
                            data.setModels(models_string);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                int versionCode = data.getVersionCode();

                PackageManager manager = activity.getPackageManager();
                PackageInfo info;
                int currentVersion = 0;
                try {
                    info = manager.getPackageInfo(activity.getPackageName(), 0);
                    currentVersion = info.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                if (versionCode == currentVersion || versionCode < currentVersion) {
                    //Updated
                } else {
                    if (data.isAll()) {
                        letUpdate(data);
                    } else {
                        String my_model = Build.MANUFACTURER.toLowerCase();
                        int my_version = Build.VERSION.SDK_INT;
                        boolean match_model = false, match_version = false;

                        for (String string : data.getModels()) {
                            if (my_model.equalsIgnoreCase(string)) {
                                match_model = true;
                                break;
                            }
                        }


                        for (int i : data.getVersions()) {
                            if (my_version == i) {
                                match_version = true;
                                break;
                            }
                        }

                        if (match_model && match_version) {
                            letUpdate(data);
                        }
                    }
                }
        }
    }

    private static void letUpdate(UpdateModel model){
        if (dialog!=null){
            dialog.dismiss();
        }

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_title,null);
        TextView title = view.findViewById(R.id.title);
        title.setText(model.getTitle());
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setCustomTitle(view)
                .setMessage(model.getMessage())
                .setCancelable(!model.isForce());

        if (model.getPlaystore()!=null && !model.getPlaystore().isEmpty()){
            builder.setPositiveButton("Play Store",null);
        }


        if (model.getDownload()!=null && !model.getDownload().isEmpty()){
            builder.setNegativeButton("Website",null);
        }

        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                //Website
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(view1 -> {
                    if (model.isUninstall()){
                        uninstall();
                    }
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(model.getDownload())));
                    dialog.show();
                });

                //Play store
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view12 -> {
                    final String appPackageName = model.getPlaystore();
                    if (model.isUninstall()){
                        uninstall();
                    }
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    }
                    dialog.show();
                });
            }
        });
        dialog.show();
    }

    private static void uninstall(){
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:"+activity.getPackageName()));
        activity.startActivity(intent);
    }
}
