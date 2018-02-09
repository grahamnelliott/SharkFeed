package com.ge.grahamelliott.sharkfeed.photolist;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ge.grahamelliott.sharkfeed.R;
import com.ge.grahamelliott.sharkfeed.photolist.views.PhotoItemView;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author graham.elliott
 */
@Singleton
public class PhotoRecyclerAdapter extends RecyclerView.Adapter<PhotoRecyclerAdapter.PhotoViewHolder>
        implements PhotoClickListener {

    private PhotoListPresenter presenter;

    @Inject
    public PhotoRecyclerAdapter(PhotoListPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhotoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent,
                                                                                    false), this);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        presenter.onBindPhotoViewAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getPhotoViewCount();
    }

    @Override
    public void onPhotoClicked(int position) {
        presenter.onPhotoClicked(position);
    }

    /**
     * View holder for photo list item.
     */
    public static class PhotoViewHolder extends RecyclerView.ViewHolder implements PhotoItemView {

        ImageView photoView;

        SimpleTarget<Bitmap> photoTarget = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                photoView.setImageBitmap(resource);
            }
        };

        public PhotoViewHolder(final View itemView, final PhotoClickListener listener) {
            super(itemView);
            photoView = itemView.findViewById(R.id.img_photo);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPhotoClicked(getAdapterPosition());
                }
            });
        }

        @Override
        public void setImageFromUri(Uri uri) {
            ViewCompat.setTransitionName(photoView, uri.toString());
            Glide.with(itemView.getContext())
                 .asBitmap()
                 .load(uri)
                 .into(photoTarget);
        }
    }
}
