package com.emreeran.android.roomsample.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.db.SampleDb;
import com.emreeran.android.roomsample.db.UserDao;
import com.emreeran.android.roomsample.ui.common.DiffListAdapter;
import com.emreeran.android.roomsample.ui.post.PostActivity;
import com.emreeran.android.roomsample.vo.User;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable mDisposables;
    private UserAdapter mUserAdapter;
    private RecyclerView mUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDisposables = new CompositeDisposable();
        mUserAdapter = new UserAdapter();
        mUserList = findViewById(R.id.users);
        mUserList.setAdapter(mUserAdapter);
        createAndListUsers();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposables.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposables.dispose();
    }

    private void createAndListUsers() {
        final UserDao userDao = SampleDb.getInstance(this).userDao();
        new CompletableFromAction(() -> {
            User alice = new User("Alice");
            User bob = new User("Bob");
            User charlie = new User("Charlie");
            User dave = new User("Dave");
            User erin = new User("Erin");
            userDao.insertAll(alice, bob, charlie, dave, erin);
        })
                .andThen(userDao.listAll())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposables.add(d);
                    }

                    @Override
                    public void onSuccess(List<User> users) {
                        mUserAdapter.replace(users);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("main", e.getMessage(), e);
                    }
                });
    }

    class UserAdapter extends DiffListAdapter<User, UserHolder> {

        @Override
        protected boolean areItemsTheSame(User oldItem, User newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        protected boolean areContentsTheSame(User oldItem, User newItem) {
            return oldItem.name.equals(newItem.name);
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new UserHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false));
        }

        @Override
        protected void bind(User item, UserHolder holder) {
            holder.setItem(item);
        }
    }

    class UserHolder extends RecyclerView.ViewHolder {
        UserHolder(View itemView) {
            super(itemView);
        }

        void setItem(User user) {
            TextView nameView = itemView.findViewById(R.id.name);
            nameView.setText(user.name);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                intent.putExtra("userId", user.id);
                startActivity(intent);
            });
        }
    }
}
