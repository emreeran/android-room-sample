package com.emreeran.android.roomsample.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.emreeran.android.roomsample.vo.Post;
import com.emreeran.android.roomsample.vo.PostWithLikesAndUser;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Emre Eran on 24.11.2017.
 */
@Dao
public interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Transaction
    @Query("SELECT p.PostId, p.PostContent, p.PostCreatedAt, " +
            "postUser.UserId, postUser.UserName, postUser.UserCreatedAt, " +
            "l.LikeId, l.LikeCreatedAt," +
            "likeUser.UserId as LikeUserId, likeUser.UserName as LikeUserName, likeUser.UserCreatedAt as LikeUserCreatedAt " +
            "FROM posts as p " +
            "JOIN users as postUser on postUser.UserId = p.PostUserId " +
            "LEFT JOIN relationships as r on postUser.UserId = r.RelationshipFollowedId " +
            "LEFT JOIN users as follower on r.RelationshipFollowerId = postUser.UserId " +
            "LEFT JOIN likes as l on l.LikePostId = p.PostId " +
            "LEFT JOIN users as likeUser on  likeUser.UserId = l.LikeUserId " +
            "GROUP BY p.PostId " +
            "ORDER BY p.PostCreatedAt DESC")
    Flowable<List<PostWithLikesAndUser>> listPostsWithLikesAndUser();
}
