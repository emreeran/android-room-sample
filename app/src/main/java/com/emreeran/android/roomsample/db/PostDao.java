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
            "u1.UserId, u1.UserName, u1.UserCreatedAt, " +
            "l.LikeId, l.LikeCreatedAt," +
            "u2.UserId as LikeUserId, u2.UserName as LikeUserName, u2.UserCreatedAt as LikeUserCreatedAt " +
            "FROM posts as p " +
            "JOIN users as u1 on u1.UserId = p.PostUserId " +
            "LEFT JOIN likes as l on l.LikePostId = p.PostId " +
            "LEFT JOIN users as u2 on  u2.UserId = l.LikeUserId " +
            "GROUP BY p.PostId " +
            "ORDER BY p.PostCreatedAt DESC")
    Flowable<List<PostWithLikesAndUser>> listPostsWithLikesAndUser();
}
