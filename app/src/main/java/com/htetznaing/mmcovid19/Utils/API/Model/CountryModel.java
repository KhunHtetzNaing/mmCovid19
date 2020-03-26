package com.htetznaing.mmcovid19.Utils.API.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.htetznaing.mmcovid19.Constants;

public class CountryModel {

    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("cases")
    @Expose
    private String cases;
    @SerializedName("todayCases")
    @Expose
    private String todayCases;
    @SerializedName("deaths")
    @Expose
    private String deaths;
    @SerializedName("todayDeaths")
    @Expose
    private String todayDeaths;
    @SerializedName("recovered")
    @Expose
    private String recovered;
    @SerializedName("active")
    @Expose
    private String active;
    @SerializedName("critical")
    @Expose
    private String critical;
    @SerializedName("casesPerOneMillion")
    @Expose
    private String casesPerOneMillion;
    @SerializedName("deathsPerOneMillion")
    @Expose
    private String deathsPerOneMillion;

    public String getCountryNameInEnglish() {
        return country;
    }

//    CountryInfo countryInfo;

//    public CountryInfo getCountryInfo() {
//        return countryInfo;
//    }

//    public void setCountryInfo(CountryInfo countryInfo) {
//        this.countryInfo = countryInfo;
//    }

    public String getCountry() {
        String mmCountry = CountryUtils.getCountryInMM(CountryUtils.getCountryCode(country));
        return mmCountry.equalsIgnoreCase("no data") ? country : mmCountry;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCases() {
        return Constants.notNullString(Constants.toMmNum(cases));
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getTodayCases() {
        return Constants.notNullString(Constants.toMmNum(todayCases));
    }

    public void setTodayCases(String todayCases) {
        this.todayCases = todayCases;
    }

    public String getDeaths() {
        return Constants.notNullString(Constants.toMmNum(deaths));
    }

    public void setDeaths(String deaths) {

        this.deaths = deaths;
    }

    public String getTodayDeaths() {
        return Constants.notNullString(Constants.toMmNum(todayDeaths));
    }

    public void setTodayDeaths(String odayDeaths) {
        this.todayDeaths = odayDeaths;
    }

    public String getRecovered() {
        return Constants.notNullString(Constants.toMmNum(recovered));
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getActive() {
        return Constants.notNullString(Constants.toMmNum(active));
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCritical() {
        return Constants.notNullString(Constants.toMmNum(critical));
    }

    public void setCritical(String critical) {
        this.critical = critical;
    }

    public String getCasesPerOneMillion() {
        return Constants.notNullString(Constants.toMmNum(casesPerOneMillion));
    }

    public void setCasesPerOneMillion(String casesPerOneMillion) {
        this.casesPerOneMillion = casesPerOneMillion;
    }
}
