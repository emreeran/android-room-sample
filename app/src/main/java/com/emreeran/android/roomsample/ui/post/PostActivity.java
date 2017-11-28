package com.emreeran.android.roomsample.ui.post;

import android.content.Context;
import android.content.Intent;
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
import com.emreeran.android.roomsample.db.RelationshipDao;
import com.emreeran.android.roomsample.db.SampleDb;
import com.emreeran.android.roomsample.db.UserDao;
import com.emreeran.android.roomsample.ui.common.DiffListAdapter;
import com.emreeran.android.roomsample.ui.user.UserActivity;
import com.emreeran.android.roomsample.vo.Like;
import com.emreeran.android.roomsample.vo.Post;
import com.emreeran.android.roomsample.vo.PostWithLikesAndUser;
import com.emreeran.android.roomsample.vo.Relationship;
import com.emreeran.android.roomsample.vo.User;

import java.text.SimpleDateFormat;
import java.util.List;
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

public class PostActivity extends AppCompatActivity {
    private static final String TAG = PostActivity.class.getName();

    private String mUserId;

    private CompositeDisposable mDisposables;

    private TextView mUserName;
    private EditText mPostInput;
    private Button mPostButton;
    private Button mUserInfoButton;
    private RecyclerView mPostList;

    private PostAdapter mPostAdapter;

    private User mUser;

    private SimpleDateFormat mPostDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (getIntent().getExtras() != null && getIntent().getExtras().getString("userId") != null) {
            mUserId = getIntent().getExtras().getString("userId");
        } else {
            throw new IllegalArgumentException("User activity needs a user id.");
        }

        mDisposables = new CompositeDisposable();

        mUserName = findViewById(R.id.user_name);
        mPostInput = findViewById(R.id.post_input);
        mPostButton = findViewById(R.id.post_button);
        mPostList = findViewById(R.id.posts);
        mUserInfoButton = findViewById(R.id.user_info_button);

        mPostAdapter = new PostAdapter();
        mPostAdapter.setOnItemsReplacedListener((oldSize, newSize) -> {
            if (oldSize != newSize) {
                mPostList.getLayoutManager().scrollToPosition(0);
            }
        });
        mPostList.setAdapter(mPostAdapter);

        mPostButton.setOnClickListener(v -> post());
        mUserInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserActivity.class);
            intent.putExtra("userId", mUserId);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        UserDao userDao = SampleDb.getInstance(this).userDao();
        userDao.findById(mUserId)
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
                        postsWithLikesAndUsers -> mPostAdapter.replace(postsWithLikesAndUsers),
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

    private Relationship containsFollower(final List<Relationship> items, String followerId) {
        for (Relationship item : items) {
            if (item.followerId.equals(followerId)) {
                return item;
            }
        }
        return null;
    }

    class PostAdapter extends DiffListAdapter<PostWithLikesAndUser, PostHolder> {
        @Override
        protected boolean areItemsTheSame(PostWithLikesAndUser oldItem, PostWithLikesAndUser newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        protected boolean areContentsTheSame(PostWithLikesAndUser oldItem, PostWithLikesAndUser newItem) {
            return oldItem.equals(newItem);
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
        TextView mLikeCountView;
        TextView mFollowerCountView;
        Button mLikeButton;
        Button mFollowButton;

        PostHolder(View itemView) {
            super(itemView);
            mContentView = itemView.findViewById(R.id.post_text);
            mDateView = itemView.findViewById(R.id.post_date);
            mUserNameView = itemView.findViewById(R.id.post_user_name);
            mLikeCountView = itemView.findViewById(R.id.like_count);
            mFollowerCountView = itemView.findViewById(R.id.follower_count);
            mLikeButton = itemView.findViewById(R.id.like_button);
            mFollowButton = itemView.findViewById(R.id.follow_button);
        }

        void setPost(PostWithLikesAndUser post) {
            mContentView.setText(post.content);
            mDateView.setText(mPostDateFormat.format(post.createdAt));
            mUserNameView.setText(post.user.name);
            int likeCount = post.likes.size();
            String likeCountText;
            if (likeCount == 1) {
                likeCountText = likeCount + " Like";
            } else {
                likeCountText = likeCount + " Likes";
            }
            mLikeCountView.setText(likeCountText);

            mLikeButton.setOnClickListener(v -> {
                mLikeButton.setEnabled(false);
                LikeDao likeDao = SampleDb.getInstance(itemView.getContext()).likeDao();
                Like like = new Like(mUserId, post.id);
                mDisposables.add(new CompletableFromAction(() -> likeDao.insert(like))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> mLikeButton.setEnabled(true), throwable -> {
                            mLikeButton.setEnabled(true);
                            Log.e(TAG, "Like insert failed.", throwable);
                        }));
            });

            int followerCount = post.user.followers.size();
            String followerCountText;
            if (followerCount == 1) {
                followerCountText = followerCount + " Follower";
            } else {
                followerCountText = followerCount + " Followers";
            }
            mFollowerCountView.setText(followerCountText);

            if (post.user.id.equals(mUserId)) {
                mFollowButton.setVisibility(View.INVISIBLE);
            } else {
                mFollowButton.setVisibility(View.VISIBLE);
                RelationshipDao relationshipDao = SampleDb.getInstance(itemView.getContext()).relationshipDao();

                Relationship follower = containsFollower(post.user.followers, mUserId);
                if (follower != null) {
                    mFollowButton.setText(R.string.unfollow);
                    mFollowButton.setOnClickListener(v -> mDisposables.add(
                            new CompletableFromAction(() -> relationshipDao.delete(follower))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> mFollowButton.setEnabled(true), throwable -> {
                                        mFollowButton.setEnabled(true);
                                        Log.e(TAG, "Unfollow request failed.");
                                    })));
                } else {
                    mFollowButton.setText(R.string.follow);
                    mFollowButton.setOnClickListener(v -> {
                        mFollowButton.setEnabled(false);
                        Relationship relationship = new Relationship(mUserId, post.user.id, Relationship.Status.PENDING);
                        mDisposables.add(new CompletableFromAction(() -> relationshipDao.insert(relationship))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> mFollowButton.setEnabled(true), throwable -> {
                                    mFollowButton.setEnabled(true);
                                    Log.e(TAG, "Follow request failed.");
                                }));
                    });
                }
            }
        }
    }
}
