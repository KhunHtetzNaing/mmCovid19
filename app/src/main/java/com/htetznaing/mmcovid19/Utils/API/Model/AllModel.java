package com.htetznaing.mmcovid19.Utils.API.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllModel {
    @SerializedName("cases")
    @Expose
    String cases;

    @SerializedName("deaths")
    @Expose
    String deaths;

    @SerializedName("recovered")
    @Expose
    String recovered;

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }
}
