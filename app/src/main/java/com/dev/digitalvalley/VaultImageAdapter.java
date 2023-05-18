package com.dev.digitalvalley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class VaultImageAdapter extends RecyclerView.Adapter<VaultImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<UploadVaultDocument> mUploads;

    public VaultImageAdapter(Context vaultContext, List<UploadVaultDocument> vaultUploads) {
        mContext = vaultContext;
        mUploads = vaultUploads;

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.vault_image_itemview, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        UploadVaultDocument uploadVaultDocumentCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadVaultDocumentCurrent.getImageName());
        Glide.with(mContext).load(uploadVaultDocumentCurrent.getImageUrl()).placeholder(R.mipmap.ic_launcher).fitCenter().centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageView;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.vault_image_name);
            imageView = itemView.findViewById(R.id.vault_image_preview);
        }
    }
}
