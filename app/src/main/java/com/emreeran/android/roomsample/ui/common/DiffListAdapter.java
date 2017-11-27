package com.emreeran.android.roomsample.ui.common;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.reactivex.annotations.Nullable;

/**
 * Created by Emre Eran on 26.11.2017.
 */

public abstract class DiffListAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    @Nullable
    private List<T> mItems;
    private int mDataVersion = 0;

    private OnItemsReplacedListener mOnItemsReplacedListener;

    protected abstract boolean areItemsTheSame(T oldItem, T newItem);

    protected abstract boolean areContentsTheSame(T oldItem, T newItem);

    protected abstract void bind(T item, VH holder);

    @Override
    public final void onBindViewHolder(VH holder, int position) {
        bind(mItems.get(position), holder);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @SuppressLint("StaticFieldLeak")
    @MainThread
    public void replace(List<T> update) {
        mDataVersion++;
        if (mItems == null) {
            if (update == null) {
                return;
            }
            mItems = update;
            notifyDataSetChanged();
        } else if (update == null) {
            int oldSize = mItems.size();
            mItems = null;
            notifyItemRangeRemoved(0, oldSize);
        } else {
            final int startVersion = mDataVersion;
            final List<T> oldItems = mItems;
            new AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                @Override
                protected DiffUtil.DiffResult doInBackground(Void... voids) {
                    return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                        @Override
                        public int getOldListSize() {
                            return oldItems.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return update.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = update.get(newItemPosition);
                            return DiffListAdapter.this.areItemsTheSame(oldItem, newItem);
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = update.get(newItemPosition);
                            return DiffListAdapter.this.areContentsTheSame(oldItem, newItem);
                        }
                    });
                }

                @Override
                protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                    if (startVersion != mDataVersion) {
                        // ignore update
                        return;
                    }
                    mItems = update;
                    diffResult.dispatchUpdatesTo(DiffListAdapter.this);
                    if (mOnItemsReplacedListener != null) {
                        mOnItemsReplacedListener.onItemsReplaced();
                    }
                }
            }.execute();
        }
    }

    public interface OnItemsReplacedListener {
        void onItemsReplaced();
    }

    public void setOnItemsReplacedListener(OnItemsReplacedListener onItemsReplacedListener) {
        mOnItemsReplacedListener = onItemsReplacedListener;
    }
}
