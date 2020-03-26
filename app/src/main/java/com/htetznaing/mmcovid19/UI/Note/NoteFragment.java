package com.htetznaing.mmcovid19.UI.Note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.htetznaing.mmcovid19.Adapter.NoteAdapter;
import com.htetznaing.mmcovid19.Constants;
import com.htetznaing.mmcovid19.MainActivity;
import com.htetznaing.mmcovid19.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment {
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private List<String> data = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager =new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NoteAdapter(data);
        recyclerView.setAdapter(adapter);

        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String all) {
                List<String> temp = parseData(all);
                data.clear();
                data.addAll(temp);
                adapter.notifyDataSetChanged();
            }
        };
        if (MainActivity.instance!=null && MainActivity.noteViewModel!=null) {
            MainActivity.noteViewModel.getNote().observe(MainActivity.instance, nameObserver);
        }

        root.findViewById(R.id.feedback).setOnClickListener(view -> {
            Constants.showFeedback(getContext());
        });
        return root;
    }

    private List<String> parseData(String data){
        List<String> temp = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i=0;i<jsonArray.length();i++){
                temp.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
