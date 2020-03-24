package com.htetznaing.covid_19counter.Utils.API;

import android.os.AsyncTask;
import android.os.Handler;

import com.htetznaing.covid_19counter.Utils.API.Model.AllModel;
import com.htetznaing.covid_19counter.Utils.API.Model.CountryModel;
import com.htetznaing.covid_19counter.Utils.API.Model.CountryUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Covid19API {
    private OnLoaded onAllLoaded;
    private Handler mHandler=new Handler();
    private boolean mStopHandler = false;

    public void load(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!mStopHandler) {
                    new AsyncTask<Void,Void,Document>(){

                        @Override
                        protected Document doInBackground(Void... voids) {
                            try {
                                return Jsoup.connect("https://www.worldometers.info/coronavirus/").get();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Document document) {
                            super.onPostExecute(document);
                            if (document!=null){
                                //All
                                AllModel model = getAll(document);
                                //Countries
                                List<CountryModel> today = parseCountry(document.getElementById("main_table_countries_today").getElementsByTag("tbody").get(0).getElementsByTag("tr"));
                                List<CountryModel> yesterday = parseCountry(document.getElementById("main_table_countries_yesterday").getElementsByTag("tbody").get(0).getElementsByTag("tr"));
                                if (onAllLoaded!=null){
                                    onAllLoaded.allLoaded(model);
                                    onAllLoaded.countriesLoaded(new List[]{today, yesterday});
                                }
                            }
                        }
                    }.execute();
                    mHandler.postDelayed(this, 15000);
                }
            }
        };
        mHandler.post(runnable);
    }

    private List<CountryModel> parseCountry(Elements today){
        List<CountryModel> data = new ArrayList<>();
        for (Element e:today){
            CountryModel countryModel = new CountryModel();
            Elements temp = e.getElementsByTag("td");

            String country = notNullString(temp.get(0).text());
            String cases = notNullString(temp.get(1).text());
            String todayCases = notNullString(temp.get(2).text());
            String deaths = notNullString(temp.get(3).text());
            String todayDeaths = notNullString(temp.get(4).text());
            String recovered = notNullString(temp.get(5).text());
            String active = notNullString(temp.get(6).text());
            String critical = notNullString(temp.get(7).text());
            String casesPerOneMillion = notNullString(temp.get(8).text());

            countryModel.setCountryNameInEnglish(country);
            String mmCountry = CountryUtils.getCountryInMM(CountryUtils.getCountryCode(country));
            System.out.println(country +" | "+mmCountry);
            country = mmCountry.equalsIgnoreCase("no data") ? country : mmCountry;

            countryModel.setCountry(country);
            countryModel.setCases(toMmNum(cases));
            countryModel.setTodayCases(toMmNum(todayCases));
            countryModel.setDeaths(toMmNum(deaths));
            countryModel.setTodayDeaths(toMmNum(todayDeaths));
            countryModel.setRecovered(toMmNum(recovered));
            countryModel.setActive(toMmNum(active));
            countryModel.setCritical(toMmNum(critical));
            countryModel.setCasesPerOneMillion(toMmNum(casesPerOneMillion));
            countryModel.setCountryInfo(CountryUtils.getCountryData(country,CountryUtils.getCountryCode(country)));

            data.add(countryModel);
        }
        return data;
    }

    public void onLoadedListerner(OnLoaded onAllLoaded){
        this.onAllLoaded=onAllLoaded;
    }

    private String toMmNum(String num){
        num = num.replaceAll("\\D","");
        String out = "";
        for (String a:num.split("")){
            out+=replace(a);
        }
        return out;
    }

    private String replace(String number){
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
    private String notNullString(String a){
        a = a.trim();
        if (a==null || a.isEmpty()){
            return "0";
        }
        return a;
    }

    private AllModel getAll(Document document) {
        Elements elements = document.getElementsByClass("maincounter-number");
        AllModel allModel = new AllModel();
        for (int i=0;i<elements.size();i++){
            if (i==0){
                allModel.setCases(toMmNum(notNullString(elements.get(i).text())));
            }else if (i==1){
                allModel.setDeaths(toMmNum(notNullString(elements.get(i).text())));
            }else if (i==2){
                allModel.setRecovered(toMmNum(notNullString(elements.get(i).text())));
            }
        }
        return allModel;
    }

    public interface OnLoaded{
        void allLoaded(AllModel all);
        void countriesLoaded(List<CountryModel> countries[]);
    }
}
