package com.ge.grahamelliott.sharkfeed.photodetail;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ge.grahamelliott.sharkfeed.R;
import com.ge.grahamelliott.sharkfeed.SharkFeedApplication;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Fragment to display enlarged photo and options.
 *
 * @author graham.elliott
 */
public class PhotoDetailFragment extends Fragment implements PhotoDetailView {

    public static final String TAG = PhotoDetailFragment.class.getName();

    @Inject
    PhotoDetailPresenter presenter;

    @BindView(R.id.img_photo)
    ImageView imageView;

    @BindView(R.id.txt_title)
    TextView titleView;

    @BindView(R.id.txt_description)
    TextView descriptionView;

//    @BindView(R.id.)

    private Unbinder unbinder;

    public static PhotoDetailFragment newInstance(int photoPosition) {
        Bundle args = new Bundle();
        args.putInt("photo", photoPosition);
        PhotoDetailFragment fragment = new PhotoDetailFragment();
        fragment.setArguments(args);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.setSharedElementEnterTransition(new DetailsTransition());
            fragment.setSharedElementReturnTransition(new DetailsTransition());
            fragment.setEnterTransition(new Fade());
            fragment.setExitTransition(new Fade());
        }

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        SharkFeedApplication.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.frag_photo_detail, container, false);
        unbinder = ButterKnife.bind(this, layout);

        int photoPosition = getArguments().getInt("photo");
        presenter.bindView(this, photoPosition);
        ViewCompat.setTransitionName(imageView, String.format("detail%s", photoPosition));

        return layout;
    }

    @Override
    public void setSharedElementEnterTransition(Object transition) {
        super.setSharedElementEnterTransition(transition);
    }

    @Override
    public void setSharedElementReturnTransition(Object transition) {
        super.setSharedElementReturnTransition(transition);
    }

    @OnClick(R.id.btn_download)
    public void onDownloadClicked() {
        presenter.downloadImageToDevice();
    }

    @OnClick(R.id.btn_open_in_web)
    public void onOpenInWebClicked() {
        presenter.openImageInWeb();
    }

    @Override
    public void loadImageFromUri(Uri thumbnailUri, Uri fullSizeUri) {
        RequestBuilder<Drawable> thumbnail = null;
        if (fullSizeUri != thumbnailUri) {
             thumbnail = Glide.with(this).load(thumbnailUri);
        }

        RequestBuilder<Drawable> full = Glide.with(this).load(fullSizeUri);
        if (thumbnail != null) {
            full.thumbnail(thumbnail).into(imageView);
        } else {
            full.into(imageView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setTitleText(String title) {
        titleView.setText(title);
    }

    @Override
    public void saveImageFromUri(Uri uri) {
        SimpleTarget<Bitmap> bitTarget = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                presenter.saveBitmapToDevice(bitmap);
            }
        };

        Glide.with(this).asBitmap().load(uri).into(bitTarget);
    }

    @Override
    public void showImageSavedToast() {
        showToastWithText("Image saved");
    }

    @Override
    public void showImageSaveFailureToast() {
        showToastWithText("Save failed");
    }

    @Override
    public void showPhotoDetailLoadFailureToast() {
        showToastWithText("Failed to load image details");
    }

    @Override
    public void updateImageAttributes(String title, String description, String views) {

    }

    @Override
    public void launchWebpage(Uri uri) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(webIntent);
    }

    private void showToastWithText(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @TargetApi(21)
    static class DetailsTransition extends TransitionSet {
        DetailsTransition() {
            setOrdering(ORDERING_TOGETHER);
            addTransition(new ChangeBounds()).addTransition(new ChangeTransform())
                                             .addTransition(new ChangeImageTransform());
        }
    }
}
