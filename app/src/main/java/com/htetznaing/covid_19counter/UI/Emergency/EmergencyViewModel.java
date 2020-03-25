package com.htetznaing.covid_19counter.UI.Emergency;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmergencyViewModel extends ViewModel {
    // Create a LiveData with a String
    private MutableLiveData<String> emg;

    public MutableLiveData<String> getEmergency() {
        if (emg == null) {
            emg = new MutableLiveData<>();
        }
        return emg;
    }
}