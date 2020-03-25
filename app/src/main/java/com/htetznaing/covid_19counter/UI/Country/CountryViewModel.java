package com.htetznaing.covid_19counter.UI.Country;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.htetznaing.covid_19counter.Utils.API.Model.CountryModel;

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