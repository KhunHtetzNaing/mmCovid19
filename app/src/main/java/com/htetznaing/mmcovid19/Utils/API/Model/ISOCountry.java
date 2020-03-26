package com.htetznaing.mmcovid19.Utils.API.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ISOCountry {

    @SerializedName("ccode")
    @Expose
    private String ccode;
    @SerializedName("cname")
    @Expose
    private String cname;

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

}