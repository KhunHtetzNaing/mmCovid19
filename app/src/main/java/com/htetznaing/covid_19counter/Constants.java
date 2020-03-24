package com.htetznaing.covid_19counter;

import androidx.annotation.NonNull;

public class Constants {
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
}
