package com.ge.grahamelliott.sharkfeed.photolist;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ge.grahamelliott.sharkfeed.R;
import com.ge.grahamelliott.sharkfeed.SharkFeedApplication;
import com.ge.grahamelliott.sharkfeed.photodetail.PhotoDetailFragment;
import com.ge.grahamelliott.sharkfeed.photolist.views.PhotoListView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment to display list of shark photos.
 *
 * @author graham.elliott
 */
public class PhotoListFragment extends Fragment implements PhotoListView {

    public static final String TAG = PhotoListFragment.class.getName();

    private static final int PHOTO_COLUMNS = 3;

    @BindView(R.id.photo_list)
    RecyclerView photoRecycler;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    PhotoListPresenter presenter;

    @Inject
    PhotoRecyclerAdapter adapter;

    public static PhotoListFragment newInstance() {
        return new PhotoListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        SharkFeedApplication.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.frag_list, container, false);
        ButterKnife.bind(this, layout);

        presenter.bindView(this);

        // set up recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), PHOTO_COLUMNS,
                                                                      LinearLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.DefaultSpanSizeLookup());
        photoRecycler.setLayoutManager(layoutManager);
        photoRecycler.setAdapter(adapter);
        photoRecycler.setHasFixedSize(true);
        photoRecycler.setItemViewCacheSize(50);
        photoRecycler.setDrawingCacheEnabled(true);
        photoRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        // using deprecated method to support < API 23
        photoRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager gm = (GridLayoutManager) recyclerView.getLayoutManager();

                int firstItem = gm.findFirstVisibleItemPosition();
                int visibleItems = gm.getChildCount();
                int totalItems = gm.getItemCount();

                presenter.updateIfAtEndOfScroll(firstItem, visibleItems, totalItems);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onSwipeToRefresh();
            }
        });

        return layout;
    }

    @Override
    public void notifyNewPhotosAdded(int newElementCount, int totalElementCount) {
        if (isAdded()) {
            adapter.notifyItemRangeInserted(totalElementCount - newElementCount, newElementCount);
        }
    }

    @Override
    public void notifyRefreshComplete() {
        if (isAdded()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void notifyAllPhotosRefreshed() {
        if (isAdded()) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void launchPhotoDetailFragment(int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setExitTransition(new Fade());
        }
        getActivity().getSupportFragmentManager().beginTransaction()
                     .replace(R.id.container, PhotoDetailFragment.newInstance(position),
                              PhotoDetailFragment.TAG)
                     .addToBackStack(PhotoDetailFragment.TAG)
                     .addSharedElement(photoRecycler.getLayoutManager().findViewByPosition(position)
                                                    .findViewById(R.id.img_photo), "detail")
                     .commit();
    }

    @Override
    public void showFailedLoadToast() {
        if (isAdded()) {
            Toast.makeText(getContext(), "Failed to load pictures", Toast.LENGTH_SHORT).show();
        }
    }
}
