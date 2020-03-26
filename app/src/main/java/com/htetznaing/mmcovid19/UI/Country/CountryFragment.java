package com.htetznaing.mmcovid19.UI.Country;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htetznaing.mmcovid19.Adapter.CountryRecyclerViewAdapter;
import com.htetznaing.mmcovid19.MainActivity;
import com.htetznaing.mmcovid19.R;
import com.htetznaing.mmcovid19.Utils.API.Model.CountryModel;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class CountryFragment extends Fragment {
    RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private List<CountryModel> data = new ArrayList<>(),temp_data = new ArrayList<>();
    private CountryRecyclerViewAdapter adapter;
    private MaterialSearchBar searchBar;
    private boolean searchMode = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_country, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);

        adapter = new CountryRecyclerViewAdapter(data);
        layoutManager =new GridLayoutManager(getContext(), 1);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        // Create the observer which updates the UI.
        final Observer<List<CountryModel>> country = new Observer<List<CountryModel>>() {
            @Override
            public void onChanged(@Nullable final List<CountryModel> countries) {
                if (countries != null) {
                    data.clear();
                    data.addAll(countries);
                    if (!searchMode) {
                        addToRecycler();
                    }else {
                        temp_data.clear();
                        temp_data.addAll(data);
                    }
                }
            }
        };

        searchBar = root.findViewById(R.id.searchBar);

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

        if (MainActivity.instance!=null && MainActivity.countryViewModel!=null) {
            MainActivity.countryViewModel.getCountries().observe(MainActivity.instance, country);
        }
        return root;
    }

    private void searchMe(String text){
        List<CountryModel> temp = new ArrayList<>();
        for (CountryModel model : temp_data){
            if (model.getCountry().toLowerCase().contains(text.toLowerCase()) || model.getCountryNameInEnglish().toLowerCase().contains(text.toLowerCase())){
                temp.add(model);
            }
        }
        data.clear();
        data.addAll(temp);
        addToRecycler();
    }

    private void addToRecycler(){
        if (data.size()>0){
            adapter.notifyDataSetChanged();
        }else {
            Toast.makeText(getContext(), getString(R.string.not_found_data), Toast.LENGTH_SHORT).show();
        }
    }
}
