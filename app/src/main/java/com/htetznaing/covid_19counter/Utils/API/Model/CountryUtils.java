package com.htetznaing.covid_19counter.Utils.API.Model;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


//Ref => https://github.com/NovelCOVID/API/blob/master/utils/country_utils.js
public class CountryUtils {
    private static Gson gson = new Gson();
    private static List<CountryInfo> countryData = Arrays.asList(gson.fromJson(PlainJSON.countryData, CountryInfo[].class));
    private static List<ISOCountry> isoCountries = Arrays.asList(gson.fromJson(PlainJSON.isoCountries, ISOCountry[].class));
    private static JSONObject countriesInMm;

    static {
        try {
            countriesInMm = new JSONObject(PlainJSON.countriesInMM);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getCountryInMM(String countryCode){
        try {
            if (countriesInMm.has(countryCode)) {
                return countriesInMm.getString(countryCode.toUpperCase());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getCountryName(countryCode);
    }

    public static CountryInfo getCountryData(String countryName,String countryCode) {
        for (CountryInfo data : countryData) {
            if (data.getCountry().toLowerCase().equals(countryName.toLowerCase()) || data.getIso2().toLowerCase().equals(countryCode.toLowerCase())) {
                return data;
            }
        }
        return null;
    }


    public static String getCountryCode(String countryName) {
        for (ISOCountry country : isoCountries) {
            if (country.getCname().toLowerCase().equals(countryName.toLowerCase())) {
                return country.getCcode();
            }
        }

        for (CountryInfo country : countryData) {
            if (country.getCountry().toLowerCase().equals(countryName.toLowerCase())) {
                return country.getIso2();
            }
        }

        return "NO DATA";
    }

    public static String getCountryName(String countryCode) {
        for (ISOCountry country : isoCountries) {
            if (country.getCcode().toLowerCase().equals(countryCode.toLowerCase())) {
                return country.getCname();
            }
        }
        return "NO DATA";
    }
}
