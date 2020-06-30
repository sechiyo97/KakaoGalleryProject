package com.example.kakaogalleryproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.CustomViewHolder> {
    private ArrayList<String> imageList;
    private Context mContext;

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.recyclerview_image);
            mContext = view.getContext();
        }
    }

    public ImageAdapter(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_image, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final @NonNull ImageAdapter.CustomViewHolder viewholder, final int position) {
        Glide.with(mContext)
                .load(imageList.get(position))
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(viewholder.imageView); // thumbnail(0.5f) 뺐음, centerCrop().도 뺐음
    }

    @Override
    public int getItemCount() {
        return (null != imageList ? imageList.size() : 0);
    }
}