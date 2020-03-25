package com.htetznaing.covid_19counter.UI.Emergency;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.htetznaing.covid_19counter.Adapter.EmergencyAdapter;
import com.htetznaing.covid_19counter.Constants;
import com.htetznaing.covid_19counter.MainActivity;
import com.htetznaing.covid_19counter.Model.EmergencyModel;
import com.htetznaing.covid_19counter.R;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmergencyFragment extends Fragment {

    private RecyclerView recView;
    private List<EmergencyModel> data=new ArrayList<>(),temp_data = new ArrayList<>();
    private EmergencyAdapter adapter;
    private MaterialSearchBar searchBar;
    private boolean searchMode = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_emergency, container, false);
        recView = root.findViewById(R.id.recView);
        searchBar = root.findViewById(R.id.searchBar);
        setupSearch();

        recView.setHasFixedSize(true);
        adapter = new EmergencyAdapter(data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recView.setLayoutManager(layoutManager);
        recView.setAdapter(adapter);

        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String all) {
                if (all==null){
                    all = Constants.getEmergency();
                }
                List<EmergencyModel> temp = Arrays.asList(new Gson().fromJson(all,EmergencyModel[].class));
                data.clear();
                data.addAll(temp);
                addToRecycler();
            }
        };
        MainActivity.instance.emergencyViewModel.getEmergency().observe(MainActivity.instance, nameObserver);
        return root;
    }

    private void setupSearch() {
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchMe(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                searchMode = enabled;
                if (enabled){
                    temp_data.clear();
                    temp_data.addAll(data);
                }else {
                    data.clear();
                    data.addAll(temp_data);
                    addToRecycler();
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                searchMe(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        searchBar.setCardViewElevation(10);
    }

    private void addToRecycler(){
        if (data.size()>0){
            adapter.notifyDataSetChanged();
        }else {
            Toast.makeText(getContext(), getString(R.string.not_found_data), Toast.LENGTH_SHORT).show();
        }
    }

    private void searchMe(String text){
            List<EmergencyModel> temp = new ArrayList<>();
            for (EmergencyModel model : temp_data) {
                if (model.getDesc().toLowerCase().contains(text.toLowerCase()) || model.getName().toLowerCase().contains(text.toLowerCase()) || Constants.cleanNumber(model.getPhone()).contains(text.toLowerCase())) {
                    temp.add(model);
                }
            }
            data.clear();
            data.addAll(temp);
            addToRecycler();
    }

}
