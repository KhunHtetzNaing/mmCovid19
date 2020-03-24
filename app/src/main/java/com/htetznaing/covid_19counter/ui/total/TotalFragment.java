package com.htetznaing.covid_19counter.ui.total;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.htetznaing.covid_19counter.MainActivity;
import com.htetznaing.covid_19counter.R;
import com.htetznaing.covid_19counter.Utils.API.Covid19API;
import com.htetznaing.covid_19counter.Utils.API.Model.AllModel;
import com.htetznaing.covid_19counter.Utils.API.Model.CountryModel;

import java.util.List;

public class TotalFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_total, container, false);
        final TextView casesText = root.findViewById(R.id.casesText),deathsText = root.findViewById(R.id.deathsText), recoveredText = root.findViewById(R.id.recoveredText);

        // Create the observer which updates the UI.
        final Observer<AllModel> nameObserver = new Observer<AllModel>() {
            @Override
            public void onChanged(@Nullable final AllModel all) {
                if (all!=null){
                    casesText.setText(all.getCases());
                    deathsText.setText(all.getDeaths());
                    recoveredText.setText(all.getRecovered());
                }
            }
        };
        MainActivity.instance.totalViewModel.getCurrent().observe(MainActivity.instance, nameObserver);
        return root;
    }
}
