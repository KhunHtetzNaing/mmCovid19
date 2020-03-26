package com.htetznaing.mmcovid19.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.htetznaing.mmcovid19.R;
import com.htetznaing.mmcovid19.Utils.API.Model.CountryModel;

import java.util.List;

public class CountryRecyclerViewAdapter extends RecyclerView.Adapter<CountryRecyclerViewAdapter.MyViewHolder> {
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvCountry.setText(data.get(position).getCountry());
        holder.tvCases.setText(data.get(position).getCases());
        holder.tvTodayCases.setText(data.get(position).getTodayCases());
        holder.tvDeath.setText(data.get(position).getDeaths());
        holder.tvToDayDeath.setText(data.get(position).getTodayDeaths());
        holder.tvRecovered.setText(data.get(position).getRecovered());
        holder.tvActive.setText(data.get(position).getActive());
        holder.tvCritical.setText(data.get(position).getCritical());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvCountry,tvCases,tvTodayCases,tvDeath,tvToDayDeath,tvRecovered,tvActive,tvCritical;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvCases = itemView.findViewById(R.id.tvCases);
            tvTodayCases = itemView.findViewById(R.id.tvTodayCases);
            tvDeath = itemView.findViewById(R.id.tvDeath);
            tvToDayDeath = itemView.findViewById(R.id.tvTodayDeaths);
            tvRecovered = itemView.findViewById(R.id.tvRecovered);
            tvActive = itemView.findViewById(R.id.tvActive);
            tvCritical = itemView.findViewById(R.id.tvCritical);
        }
    }

    private List<CountryModel> data;

    public CountryRecyclerViewAdapter(List<CountryModel> data) {
        this.data = data;
    }
}
