package com.emreeran.android.roomsample.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.db.PostDao;
import com.emreeran.android.roomsample.db.SampleDb;
import com.emreeran.android.roomsample.db.UserDao;
import com.emreeran.android.roomsample.ui.common.DiffListAdapter;
import com.emreeran.android.roomsample.vo.Post;
import com.emreeran.android.roomsample.vo.User;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Emre Eran on 26.11.2017.
 */

public class UserActivity extends AppCompatActivity {
    private static final String TAG = UserActivity.class.getName();

    private CompositeDisposable mDisposables;

    private TextView mUserName;
    private EditText mPostInput;
    private Button mPostButton;
    private RecyclerView mPostList;

    private PostAdapter mPostAdapter;

    private User mUser;

    private SimpleDateFormat mPostDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        mDisposables = new CompositeDisposable();

        mUserName = findViewById(R.id.user_name);
        mPostInput = findViewById(R.id.post_input);
        mPostButton = findViewById(R.id.post_button);
        mPostList = findViewById(R.id.posts);

        mPostAdapter = new PostAdapter();
        mPostList.setAdapter(mPostAdapter);

        mPostButton.setOnClickListener(v -> post());
    }

    @Override
    protected void onStart() {
        super.onStart();

        UserDao userDao = SampleDb.getInstance(this).userDao();
        String userId = getIntent().getExtras().getString("userId");
        userDao.findById(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposables.add(d);
                    }

                    @Override
                    public void onSuccess(User user) {
                        mUser = user;
                        mUserName.setText(user.name);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "User find by id failed.", e);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposables.clear();
    }

    private void post() {
        PostDao postDao = SampleDb.getInstance(this).postDao();
        mPostButton.setEnabled(false);
        new CompletableFromAction(() -> {
            Post post = new Post(mPostInput.getText().toString(), mUser.id);
            postDao.insert(post);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposables.add(d);
                    }

                    @Override
                    public void onComplete() {
                        mPostButton.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPostButton.setEnabled(true);
                        Log.e(TAG, "Create post failed.", e);
                    }
                });
    }

    class PostAdapter extends DiffListAdapter<Post, PostHolder> {
        @Override
        protected boolean areItemsTheSame(Post oldItem, Post newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        protected boolean areContentsTheSame(Post oldItem, Post newItem) {
            return oldItem.content.equals(newItem.content);
        }

        @Override
        protected void bind(Post item, PostHolder holder) {
            holder.setPost(item);
        }

        @Override
        public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }
    }

    class PostHolder extends RecyclerView.ViewHolder {
        TextView mContentView;
        TextView mDateView;

        public PostHolder(View itemView) {
            super(itemView);
            mContentView = itemView.findViewById(R.id.post_text);
            mDateView = itemView.findViewById(R.id.post_date);
        }

        void setPost(Post post) {
            mContentView.setText(post.content);
            mDateView.setText(mPostDateFormat.format(post.createdAt));
        }
    }
}
