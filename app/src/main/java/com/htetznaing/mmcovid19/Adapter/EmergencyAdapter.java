package com.htetznaing.mmcovid19.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.htetznaing.mmcovid19.Constants;
import com.htetznaing.mmcovid19.Model.EmergencyModel;
import com.htetznaing.mmcovid19.R;

import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.MyViewHolder> {
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(data.get(position).getName());
        holder.desc.setText("("+data.get(position).getDesc()+")");
        holder.phone.setText(data.get(position).getPhone());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhone(view.getContext(),data.get(position).getPhone());
            }
        });
    }

    private void callPhone(Context context, String phone) {
        if (phone.contains(",")){
            String[] temp = phone.split(",");
            CharSequence[] items = new CharSequence[temp.length];
            for (int i=0;i<temp.length;i++){
                items[i] = temp[i].trim();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setCustomTitle(LayoutInflater.from(context).inflate(R.layout.dialog_title,null))
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            callPhone(context,items[i].toString());
                        }
                    })
                    .setPositiveButton(context.getString(R.string.cancel_call),null);
            builder.show();

        }else {
            letCall(context,phone);
        }
    }

    private void letCall(Context context,String number){
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + Constants.cleanNumber(number)));
        context.startActivity(dialIntent);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title,desc,phone;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            desc = itemView.findViewById(R.id.tvDesc);
            phone = itemView.findViewById(R.id.phoneNumber);
        }
    }

    private List<EmergencyModel> data;

    public EmergencyAdapter(List<EmergencyModel> data) {
        this.data = data;
    }
}
