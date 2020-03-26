package com.htetznaing.mmcovid19.UI.Country;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.htetznaing.mmcovid19.Utils.API.Model.CountryModel;

import java.util.List;

public class CountryViewModel extends ViewModel {

    // Create a LiveData with a String
    private MutableLiveData<List<CountryModel>> countries;

    public MutableLiveData<List<CountryModel>> getCountries() {
        if (countries == null) {
            countries = new MutableLiveData<>();
        }
        return countries;
    }
}