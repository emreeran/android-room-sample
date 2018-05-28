package com.emreeran.android.roomsample.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.db.SampleDb;
import com.emreeran.android.roomsample.db.dao.CommentDao;
import com.emreeran.android.roomsample.db.dao.PostDao;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Emre Eran on 11.05.2018.
 */
public class PostDetailFragment extends Fragment {

    private static final String ARGS_POST_ID = "postId";

    private CompositeDisposable mDisposables;

    public static PostDetailFragment create(int postId) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_POST_ID, postId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_detail_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        if (getArguments() != null && view != null) {

            mDisposables = new CompositeDisposable();
            int postId = getArguments().getInt(ARGS_POST_ID);

            SampleDb db = SampleDb.getInstance(getContext());
            PostDao postDao = db.postDao();
            CommentDao commentDao = db.commentDao();

            CommentAdapter commentAdapter = new CommentAdapter();
            RecyclerView commentList = view.findViewById(R.id.comment_list);
            commentList.setAdapter(commentAdapter);

            TextView nameTextView = view.findViewById(R.id.name);
            TextView contentTextView = view.findViewById(R.id.content);
            TextView likeCountTextView = view.findViewById(R.id.like_count);
            TextView commentCountTextView = view.findViewById(R.id.comment_count);
            ImageView userImageView = view.findViewById(R.id.user_image);
            ImageButton likeButton = view.findViewById(R.id.like_button);
            ImageButton commentButton = view.findViewById(R.id.comment_button);

            mDisposables.add(Single.fromCallable(() -> postDao.findByIdWithUser(postId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(postWithUser -> {
                        nameTextView.setText(postWithUser.user.name);
                        contentTextView.setText(postWithUser.post.content);
                        Glide.with(view)
                                .load(postWithUser.user.image)
                                .apply(new RequestOptions().circleCrop())
                                .into(userImageView);

                        mDisposables.add(commentDao.listByPostId(postWithUser.post.id)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(comments -> {
                                    StringBuilder commentCountText = new StringBuilder(Integer.toString(comments.size()));
                                    if (comments.size() > 1 || comments.size() == 0) {
                                        commentCountText.append(" comments");
                                    } else {
                                        commentCountText.append(" comment");
                                    }
                                    commentCountTextView.setText(commentCountText);
                                    commentAdapter.replace(comments);
                                })
                        );
                    })
            );

        }
    }
}
