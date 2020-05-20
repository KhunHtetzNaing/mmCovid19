package com.htetznaing.mmcovid19.Utils.API;

import android.os.AsyncTask;
import android.os.Handler;

import com.google.gson.Gson;
import com.htetznaing.mmcovid19.Constants;
import com.htetznaing.mmcovid19.Utils.API.Model.AllModel;
import com.htetznaing.mmcovid19.Utils.API.Model.CountryModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Covid19API {
    private OnLoaded onAllLoaded;
    private Handler mHandler=new Handler();
    public static boolean stopMMHandler;

    public void load(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do your stuff - don't create a new runnable here!
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
                            try {
                                //All
                                AllModel model = getAll(document);
                                //Countries
                                List<CountryModel> today = parseCountry(document.getElementById("main_table_countries_today").getElementsByTag("tbody").get(0).getElementsByTag("tr"));
                                if (onAllLoaded!=null){
                                    onAllLoaded.allLoaded(model);
                                    onAllLoaded.countriesLoaded(today);
                                }
                            }catch (Exception e){
                                loadFromAPI();
                            }
                        }else {
                            loadFromAPI();
                        }
                    }
                }.execute();
                if (!stopMMHandler) {
                    System.out.println("Load Again!");
                    mHandler.postDelayed(this, 20000);
                }
            }
        };
        mHandler.post(runnable);
    }

    private void loadFromAPI(){
        System.out.println("Load From API!");
        new AsyncTask<Void,Void,String[]>(){

            @Override
            protected String[] doInBackground(Void... voids) {
                try {
                    String countries = Jsoup.connect("https://corona.lmao.ninja/countries")
                            .ignoreContentType(true)
                            .get().text();
                    String all = Jsoup.connect("https://corona.lmao.ninja/all").ignoreContentType(true).get().text();
                    return new String[]{all, countries};
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String[] strings) {
                super.onPostExecute(strings);
                if (strings!=null) {
                    String all = strings[0];
                    String countries = strings[1];

                    if (onAllLoaded!=null){
                        onAllLoaded.allLoaded(new Gson().fromJson(all,AllModel.class));
                        onAllLoaded.countriesLoaded(Arrays.asList(new Gson().fromJson(countries,CountryModel[].class)));
                        System.out.println("UPDATED!");
                    }
                }else System.out.println("NULLED");
            }
        }.execute();
    }

    private List<CountryModel> parseCountry(Elements today){
        List<CountryModel> data = new ArrayList<>();
        for (Element e:today){
            if (!e.toString().contains("style=\"display: none\"")) {
                CountryModel countryModel = new CountryModel();
                Elements temp = e.getElementsByTag("td");
                String country = Constants.notNullString(temp.get(1).text());

                String cases = temp.get(2).text();
                String todayCases = temp.get(3).text();
                String deaths = temp.get(4).text();
                String todayDeaths = temp.get(5).text();
                String recovered = temp.get(6).text();
                String active = temp.get(7).text();
                String critical = temp.get(8).text();
                String casesPerOneMillion = temp.get(9).text();

                countryModel.setCountry(country);
                countryModel.setCases(cases);
                countryModel.setTodayCases(todayCases);
                countryModel.setDeaths(deaths);
                countryModel.setTodayDeaths(todayDeaths);
                countryModel.setRecovered(recovered);
                countryModel.setActive(active);
                countryModel.setCritical(critical);
                countryModel.setCasesPerOneMillion(casesPerOneMillion);
//            countryModel.setCountryInfo(CountryUtils.getCountryData(country,CountryUtils.getCountryCode(country)));
                if (!country.equalsIgnoreCase("world")) {
                    data.add(countryModel);
                }
            }
        }
        return data;
    }

    public void onLoadedListerner(OnLoaded onAllLoaded){
        this.onAllLoaded=onAllLoaded;
    }

    private AllModel getAll(Document document) {
        Elements elements = document.getElementsByClass("maincounter-number");
        AllModel allModel = new AllModel();
        for (int i=0;i<elements.size();i++){
            if (i==0){
                allModel.setCases(Constants.toMmNum(Constants.notNullString(elements.get(i).text())));
            }else if (i==1){
                allModel.setDeaths(Constants.toMmNum(Constants.notNullString(elements.get(i).text())));
            }else if (i==2){
                allModel.setRecovered(Constants.toMmNum(Constants.notNullString(elements.get(i).text())));
            }
        }
        return allModel;
    }

    public interface OnLoaded{
        void allLoaded(AllModel all);
        void countriesLoaded(List<CountryModel> countries);
    }
}
