package com.dev.bankapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Essential_RecyclerViewAdapter extends RecyclerView.Adapter<Essential_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<EssentialModel> essentialModels;
    public Essential_RecyclerViewAdapter(Context context, ArrayList<EssentialModel> essentialModels) {
        this.context = context;
        this.essentialModels = essentialModels;
    }

    @NonNull
    @Override
    public Essential_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.essentials_item_block,parent,false);
        return new Essential_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Essential_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.imageView.setImageResource(essentialModels.get(position).getItem_image());
        holder.txtTitle.setText(essentialModels.get(position).getItem_name());
        holder.txtValue.setText(essentialModels.get(position).getItem_value());
    }

    @Override
    public int getItemCount() {
        return essentialModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtTitle, txtValue;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            txtTitle = itemView.findViewById(R.id.textView);
            txtValue = itemView.findViewById(R.id.textView2);
        }
    }
}
