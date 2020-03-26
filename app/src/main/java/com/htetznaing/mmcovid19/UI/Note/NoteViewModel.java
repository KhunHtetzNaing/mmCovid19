package com.htetznaing.mmcovid19.UI.Note;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NoteViewModel extends ViewModel {
    // Create a LiveData with a String
    private MutableLiveData<String> note;

    public MutableLiveData<String> getNote() {
        if (note == null) {
            note = new MutableLiveData<>();
        }
        return note;
    }
}