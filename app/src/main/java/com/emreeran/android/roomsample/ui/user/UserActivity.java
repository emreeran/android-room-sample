package com.emreeran.android.roomsample.ui.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.db.LikeDao;
import com.emreeran.android.roomsample.db.PostDao;
import com.emreeran.android.roomsample.db.SampleDb;
import com.emreeran.android.roomsample.db.UserDao;
import com.emreeran.android.roomsample.ui.common.DiffListAdapter;
import com.emreeran.android.roomsample.vo.Like;
import com.emreeran.android.roomsample.vo.Post;
import com.emreeran.android.roomsample.vo.PostWithLikesAndUser;
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mDisposables = new CompositeDisposable();

        mUserName = findViewById(R.id.user_name);
        mPostInput = findViewById(R.id.post_input);
        mPostButton = findViewById(R.id.post_button);
        mPostList = findViewById(R.id.posts);

        mPostAdapter = new PostAdapter();
        mPostAdapter.setOnItemsReplacedListener(() -> mPostList.getLayoutManager().scrollToPosition(0));
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

        PostDao postDao = SampleDb.getInstance(this).postDao();
        mDisposables.add(postDao.listPostsWithLikesAndUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        postsWithLikesAndUsers -> {
                            for (PostWithLikesAndUser item : postsWithLikesAndUsers) {
                                Log.d(TAG, item.toString());
                            }
                            mPostAdapter.replace(postsWithLikesAndUsers);
                        },
                        throwable -> Log.e(TAG, "Post list load failed.", throwable)
                )
        );

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
                        mPostInput.getText().clear();
                        hideSoftKeyboard();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mPostButton.setEnabled(true);
                        Log.e(TAG, "Create post failed.", e);
                    }
                });
    }

    private void hideSoftKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    class PostAdapter extends DiffListAdapter<PostWithLikesAndUser, PostHolder> {
        @Override
        protected boolean areItemsTheSame(PostWithLikesAndUser oldItem, PostWithLikesAndUser newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        protected boolean areContentsTheSame(PostWithLikesAndUser oldItem, PostWithLikesAndUser newItem) {
            return oldItem.content.equals(newItem.content);
        }

        @Override
        protected void bind(PostWithLikesAndUser item, PostHolder holder) {
            holder.setPost(item);
        }

        @Override
        public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PostHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false));
        }
    }

    class PostHolder extends RecyclerView.ViewHolder {
        TextView mContentView;
        TextView mDateView;
        TextView mUserNameView;
        Button mLikeButton;

        PostHolder(View itemView) {
            super(itemView);
            mContentView = itemView.findViewById(R.id.post_text);
            mDateView = itemView.findViewById(R.id.post_date);
            mUserNameView = itemView.findViewById(R.id.post_user_name);
            mLikeButton = itemView.findViewById(R.id.like_button);
        }

        void setPost(PostWithLikesAndUser post) {
            mContentView.setText(post.content);
            mDateView.setText(mPostDateFormat.format(post.createdAt));
            mUserNameView.setText(post.user.name);

            mLikeButton.setOnClickListener(v -> {
                mLikeButton.setEnabled(false);
                LikeDao likeDao = SampleDb.getInstance(itemView.getContext()).likeDao();
                Like like = new Like(mUser.id, post.id);
                mDisposables.add(new CompletableFromAction(() -> likeDao.insert(like))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> mLikeButton.setEnabled(true), throwable -> {
                            mLikeButton.setEnabled(true);
                            Log.e(TAG, "Like insert failed.", throwable);
                        }));
            });
        }
    }
}
