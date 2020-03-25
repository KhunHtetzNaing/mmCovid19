package com.htetznaing.covid_19counter.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.htetznaing.covid_19counter.Model.MmModel;
import com.htetznaing.covid_19counter.R;
import com.htetznaing.covid_19counter.Utils.API.Covid19API;

import java.util.ArrayList;

public class MmAdapter extends RecyclerView.Adapter<MmAdapter.MyViewHolder> {
    private ArrayList<MmModel> data;
    private Context context;

    public MmAdapter(ArrayList<MmModel> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.mm_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.cases.setText(data.get(i).getCases());
        holder.counts.setText(Covid19API.toMmNum(data.get(i).getCount()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cases,counts;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cases = itemView.findViewById(R.id.cases);
            counts = itemView.findViewById(R.id.counts);
        }
    }
}
