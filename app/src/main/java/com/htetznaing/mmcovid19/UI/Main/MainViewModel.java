package com.htetznaing.mmcovid19.UI.Main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.htetznaing.mmcovid19.Utils.API.Model.AllModel;

public class MainViewModel extends ViewModel {

    // Create a LiveData with a String
    private MutableLiveData<AllModel> currentName;
    private MutableLiveData<String> mm_data;

    public MutableLiveData<String> getMm_data() {
        if (mm_data == null) {
            mm_data = new MutableLiveData<String>();
        }
        return mm_data;
    }

    public MutableLiveData<AllModel> getCurrent() {
        if (currentName == null) {
            currentName = new MutableLiveData<AllModel>();
        }
        return currentName;
    }
}