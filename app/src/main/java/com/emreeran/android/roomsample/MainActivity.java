package com.emreeran.android.roomsample;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emreeran.android.roomsample.db.SampleDb;
import com.emreeran.android.roomsample.db.UserDao;
import com.emreeran.android.roomsample.vo.User;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private SampleDb mDb;
    private CompositeDisposable mDisposables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDisposables = new CompositeDisposable();
        mDb = Room.databaseBuilder(this, SampleDb.class, "sample.db").build();
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
        final RecyclerView recyclerView = findViewById(R.id.users);

        final UserDao userDao = mDb.userDao();
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
                        UserAdapter adapter = new UserAdapter(users);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("main", e.getMessage(), e);
                    }
                });
    }

    class UserAdapter extends RecyclerView.Adapter<UserHolder> {
        List<User> mItems;

        UserAdapter(List<User> items) {
            mItems = items;
        }

        @Override
        public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new UserHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false));
        }

        @Override
        public void onBindViewHolder(UserHolder holder, int position) {
            holder.setItem(mItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    class UserHolder extends RecyclerView.ViewHolder {
        UserHolder(View itemView) {
            super(itemView);
        }

        void setItem(User user) {
            TextView nameView = itemView.findViewById(R.id.name);
            nameView.setText(user.name);
        }
    }
}
