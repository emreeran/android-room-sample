package com.emreeran.android.roomsample.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.MainActivity;
import com.emreeran.android.roomsample.db.SampleDb;
import com.emreeran.android.roomsample.db.dao.FeedDao;
import com.emreeran.android.roomsample.db.vo.FeedItem;

import java.util.Objects;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Emre Eran on 8.05.2018.
 */
public class FeedFragment extends Fragment {

    public static FeedFragment create() {
        return new FeedFragment();
    }

    private CompositeDisposable mDisposables;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDisposables = new CompositeDisposable();

        RecyclerView feedView = Objects.requireNonNull(getView()).findViewById(R.id.feed);

        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL);
        Drawable divider = getResources().getDrawable(R.drawable.feed_divider);
        itemDecoration.setDrawable(divider);
        feedView.addItemDecoration(itemDecoration);

        FeedAdapter adapter = new FeedAdapter();
        adapter.setOnItemClickListener(v -> {
            if (getActivity() != null) {
                int itemPosition = feedView.getChildLayoutPosition(v);
                FeedItem item = adapter.getListItem(itemPosition);
                ((MainActivity) getActivity()).navigateToPostDetail(item.postWithUser.post.id);
            }
        });
        feedView.setAdapter(adapter);

        SampleDb db = SampleDb.getInstance(getContext());
        FeedDao feedDao = db.feedDao();

        mDisposables.add(
                Flowable.fromCallable(feedDao::listFeedItems)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(adapter::replace)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposables.clear();
    }
}
