package com.htetznaing.covid_19counter.UI.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htetznaing.covid_19counter.Adapter.MmAdapter;
import com.htetznaing.covid_19counter.MainActivity;
import com.htetznaing.covid_19counter.Model.MmModel;
import com.htetznaing.covid_19counter.R;
import com.htetznaing.covid_19counter.Utils.API.Model.AllModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainFragment extends Fragment {
    private RecyclerView recyclerViewX;
    private TextView last_update;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView casesText = root.findViewById(R.id.casesText),deathsText = root.findViewById(R.id.deathsText), recoveredText = root.findViewById(R.id.recoveredText);

        recyclerViewX = root.findViewById(R.id.recyclerViewX);
        recyclerViewX.setHasFixedSize(true);
        last_update = root.findViewById(R.id.last_update);
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

        final Observer<String> mmObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String html) {
                if (html!=null){
                    try {
                        JSONArray array = new JSONArray(html);
                        ArrayList<MmModel> data = new ArrayList<>();
                        String update = null;
                        for (int i=0;i<array.length();i++){
                            JSONObject object = array.getJSONObject(i);
                            if (object.has("cases") && object.has("counts")){
                                MmModel model = new MmModel();
                                model.setCount(object.getString("counts"));
                                model.setCases(object.getString("cases"));
                                data.add(model);
                            }else if (object.has("updated")){
                                update = object.getString("updated");
                            }
                        }
                        MmAdapter adapter = new MmAdapter(data,getContext());

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerViewX.setLayoutManager(layoutManager);
                        recyclerViewX.setAdapter(adapter);
                        if (update!=null) {
                            last_update.setText(update + " " + getString(R.string.last_update_on));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        MainActivity.instance.totalViewModel.getCurrent().observe(MainActivity.instance, nameObserver);
        MainActivity.instance.totalViewModel.getMm_data().observe(MainActivity.instance, mmObserver);
        return root;
    }
}
