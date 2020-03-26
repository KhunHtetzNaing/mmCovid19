package com.htetznaing.mmcovid19.UI.Emergency;

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