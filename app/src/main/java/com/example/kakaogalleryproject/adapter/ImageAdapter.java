package com.example.kakaogalleryproject.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.kakaogalleryproject.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.CustomViewHolder> {
    private ArrayList<ImgInfo> imageList;
    private Context mContext;

    class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ProgressBar adapterProgressBar;

        CustomViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.recyclerview_image);
            this.adapterProgressBar = view.findViewById(R.id.adapter_progress_bar);
            mContext = view.getContext();
        }
    }

    public ImageAdapter(ArrayList<ImgInfo> imageList) {
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
        viewholder.adapterProgressBar.setVisibility(View.VISIBLE);
        Glide.with(mContext)
                .load(imageList.get(position).getHref())
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        viewholder.adapterProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewholder.adapterProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(viewholder.imageView); // thumbnail(0.5f) 뺐음, centerCrop().도 뺐음
    }

    @Override
    public int getItemCount() {
        return (null != imageList ? imageList.size() : 0);
    }
}