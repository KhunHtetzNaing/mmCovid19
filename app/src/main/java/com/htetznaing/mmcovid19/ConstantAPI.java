package com.htetznaing.mmcovid19;

import android.os.AsyncTask;
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ConstantAPI {
    private static Handler mHandler = new Handler();
    public static boolean stopMMHandler = false;
    public static void load(){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do your stuff - don't create a new runnable here!
                new AsyncTask<Void,Void,String>(){

                    @Override
                    protected String doInBackground(Void... voids) {
                        String json_url = "https://myappupdateserver.blogspot.com/p/covid19.html";
                        String s = null;
                        try {
                            Document document = Jsoup.connect(json_url).get();
                            if (document.hasClass("post-body entry-content")) {
                                //Blogspot Link
                                try{
                                    s = document.getElementsByClass("post-body entry-content").get(0).text();
                                }catch (Exception e) {
                                    s = document.getElementsByTag("body").get(0).text();
                                }
                            }else {
                                s = document.text();
                            }
                            assert s != null;
                            int start = s.indexOf("{");
                            int end = s.lastIndexOf("}")+1;
                            s = s.substring(start,end);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return s;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (s!=null){
                            try {
                                JSONObject object = new JSONObject(s);

                                //JS Function
                                String js_mohs = getString(object,"js_mohs");
                                if (js_mohs!=null){
                                    Constants.setJsFunction(js_mohs);
                                }

                                //News
                                JSONObject news = getJsonObject(object,"news");
                                if (news!=null){
                                    String feed = getString(news,"feed");
                                    if (feed!=null){
                                        Constants.setNewsFeed(feed);
                                    }

                                    String fb = getString(news,"fb");
                                    if (fb!=null){
                                        Constants.setNewsFb(fb);
                                    }

                                    String web = getString(news,"web");
                                    if (web!=null){
                                        Constants.setNewsWeb(web);
                                    }

                                }

                                //Emergency
                                if (object.has("emergency")){
                                    Constants.setEmergency(object.get("emergency").toString());
                                    MainActivity.emergencyViewModel.getEmergency().setValue(Constants.getEmergency());
                                }

                                //Myanmar
                                if (object.has("myanmar")){
                                    MainActivity.totalViewModel.getMm_data().setValue(object.get("myanmar").toString());
                                }

                                //Note
                                if (object.has("note")){
                                    MainActivity.noteViewModel.getNote().setValue(object.get("note").toString());
                                }

                                //Update
                                if (object.has("update_app")){
                                    String updateApp = object.get("update_app").toString();
                                    AppUpdater.letCheck(updateApp);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.execute();
                if (!stopMMHandler) {
                    mHandler.postDelayed(this, 20000);
                }
            }
        };
        mHandler.post(runnable);
    }

    private static JSONObject getJsonObject(JSONObject object,String key){
        if (object.has(key)){
            try {
                return object.getJSONObject(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String  getString(JSONObject object,String key){
        if (object.has(key)){
            try {
                return object.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
