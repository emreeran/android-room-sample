package com.emreeran.android.roomsample.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.emreeran.android.roomsample.R;
import com.emreeran.android.roomsample.db.vo.FeedItem;

/**
 * Created by Emre Eran on 9.05.2018.
 */
public class FeedItemViewHolder extends RecyclerView.ViewHolder {

    FeedItemViewHolder(View itemView) {
        super(itemView);
    }

    public void setItem(FeedItem item) {
        TextView nameTextView = itemView.findViewById(R.id.name);
        TextView contentTextView = itemView.findViewById(R.id.content);
        TextView likeCountTextView = itemView.findViewById(R.id.like_count);
        TextView commentCountTextView = itemView.findViewById(R.id.comment_count);
        ImageView userImageView = itemView.findViewById(R.id.user_image);
        ImageButton likeButton = itemView.findViewById(R.id.like_button);
        ImageButton commentButton = itemView.findViewById(R.id.comment_button);

        nameTextView.setText(item.postWithUser.user.name);
        contentTextView.setText(item.postWithUser.post.content);

        int likeCount = item.likes.size();
        int commentCount = item.comments.size();
        String likeCountText = itemView.getResources().getString(R.string.like_count, likeCount);
        String commentCountText = itemView.getResources().getString(R.string.comment_count, commentCount);
        likeCountTextView.setText(likeCountText);
        commentCountTextView.setText(commentCountText);

        Glide.with(itemView)
                .load(item.postWithUser.user.image)
                .apply(new RequestOptions().circleCrop())
                .into(userImageView);

        likeButton.setOnClickListener(v -> {
            boolean isLiked = v.getTag() != null && (boolean) v.getTag();
            if (isLiked) {
                ((ImageButton) v).setImageResource(R.drawable.ic_heart_outline);
            } else {
                ((ImageButton) v).setImageResource(R.drawable.ic_heart);
            }
            v.setTag(!isLiked);
        });
    }
}
