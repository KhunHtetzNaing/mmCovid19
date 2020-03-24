package com.htetznaing.covid_19counter.ui.total;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.htetznaing.covid_19counter.Utils.API.Model.AllModel;

public class TotalViewModel extends ViewModel {

    // Create a LiveData with a String
    private MutableLiveData<AllModel> currentName;

    public MutableLiveData<AllModel> getCurrent() {
        if (currentName == null) {
            currentName = new MutableLiveData<AllModel>();
        }
        return currentName;
    }
}