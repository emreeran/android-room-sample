package com.emreeran.android.roomsample.ui;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.ui.FeedItemViewHolder;
import com.emreeran.android.roomsample.ui.common.ListAdapter;
import com.emreeran.android.roomsample.db.vo.FeedItem;

/**
 * Created by Emre Eran on 9.05.2018.
 */
public class FeedAdapter extends ListAdapter<FeedItem, FeedItemViewHolder> {

    private View.OnClickListener mOnItemClickListener;

    FeedAdapter() {
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FeedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        view.setOnClickListener(mOnItemClickListener);
        return new FeedItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedItemViewHolder holder, int position) {
        holder.setItem(mItems.get(position));
    }

    @Override
    protected boolean areItemsTheSame(FeedItem oldItem, FeedItem newItem) {
        return oldItem.postWithUser.post.id == newItem.postWithUser.post.id;
    }

    @Override
    protected boolean areContentsTheSame(FeedItem oldItem, FeedItem newItem) {
        return oldItem.equals(newItem);
    }
}
