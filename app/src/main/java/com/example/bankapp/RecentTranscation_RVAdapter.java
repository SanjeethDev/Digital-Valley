package com.example.bankapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecentTranscation_RVAdapter extends RecyclerView.Adapter<RecentTranscation_RVAdapter.MyViewHolder> {
    Context context;
    ArrayList<RecentTransactionModel> ReTrModels;
    public RecentTranscation_RVAdapter(Context context, ArrayList<RecentTransactionModel> ReTrModels) {
        this.context = context;
        this.ReTrModels = ReTrModels;
    }
    @NonNull
    @Override
    public RecentTranscation_RVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transactionlog_itemblock, parent, false);
        return new RecentTranscation_RVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentTranscation_RVAdapter.MyViewHolder holder, int position) {
        if (ReTrModels.get(position).getTrancamount() == "+") {
            holder.tlog_accnameT.setText(ReTrModels.get(position).getAccname());
            holder.tlog_datetimeT.setText(ReTrModels.get(position).getTrancdate());
            holder.tlog_amountT.setText(ReTrModels.get(position).getTrancamount());
            holder.tlog_amountT.setTextColor(Color.rgb(121,216,87));
            holder.tlog_typeT.setText(ReTrModels.get(position).getTranctype());
            holder.tlog_amountT.setTextColor(Color.rgb(121,216,87));

        } else {
            holder.tlog_accnameT.setText(ReTrModels.get(position).getAccname());
            holder.tlog_datetimeT.setText(ReTrModels.get(position).getTrancdate());
            holder.tlog_amountT.setText(ReTrModels.get(position).getTrancamount());
            holder.tlog_amountT.setTextColor(Color.rgb(216,87,87));
            holder.tlog_typeT.setText(ReTrModels.get(position).getTranctype());
            holder.tlog_typeT.setTextColor(Color.rgb(216,87,87));
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tlog_accnameT, tlog_datetimeT, tlog_amountT, tlog_typeT;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tlog_accnameT = itemView.findViewById(R.id.tlog_accname);
            tlog_datetimeT = itemView.findViewById(R.id.tlog_datetime);
            tlog_amountT = itemView.findViewById(R.id.tlog_amount);
            tlog_typeT = itemView.findViewById(R.id.tlog_type);
        }
    }
}
