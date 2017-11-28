package com.emreeran.android.roomsample.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.db.SampleDb;
import com.emreeran.android.roomsample.db.UserDao;
import com.emreeran.android.roomsample.ui.common.DiffListAdapter;
import com.emreeran.android.roomsample.vo.Relationship;
import com.emreeran.android.roomsample.vo.User;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Emre Eran on 28.11.2017.
 */

public class UserActivity extends AppCompatActivity {

    private String mUserId;
    private RecyclerView mFollowerList;
    private FollowerAdapter mFollowerAdapter;
    private CompositeDisposable mDisposables;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mDisposables = new CompositeDisposable();
        mFollowerList = findViewById(R.id.follower_list);
        mFollowerAdapter = new FollowerAdapter();
        mFollowerList.setAdapter(mFollowerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().getExtras() != null && getIntent().getExtras().getString("userId") != null) {
            mUserId = getIntent().getExtras().getString("userId");
        } else {
            throw new IllegalArgumentException("User activity needs a user id.");
        }

        UserDao userDao = SampleDb.getInstance(this).userDao();
        mDisposables.add(userDao.listFollowersByStatus(mUserId, Relationship.Status.PENDING)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    mFollowerAdapter.replace(users);
                })
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposables.clear();
    }

    class FollowerAdapter extends DiffListAdapter<User, UserHolder> {
        @Override
        protected boolean areItemsTheSame(User oldItem, User newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        protected boolean areContentsTheSame(User oldItem, User newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        protected void bind(User item, UserHolder holder) {
            holder.setUser(item);
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new UserHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item, parent, false));
        }
    }

    class UserHolder extends RecyclerView.ViewHolder {
        private TextView mNameView;
        private Button mActionButton;

        UserHolder(View itemView) {
            super(itemView);
            mNameView = itemView.findViewById(R.id.follower_name);
            mActionButton = itemView.findViewById(R.id.follower_action_button);
        }

        public void setUser(User user) {
            mNameView.setText(user.name);
            mActionButton.setText("Accept");
        }
    }
}
