package com.emreeran.android.roomsample.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.emreeran.android.roomsample.vo.Follower;
import com.emreeran.android.roomsample.vo.Relationship;
import com.emreeran.android.roomsample.vo.User;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Created by Emre Eran on 24.11.2017.
 */
@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(User... user);

    @Query("SELECT * FROM users WHERE users.userId = :id LIMIT 1")
    Single<User> findById(String id);

    @Query("SELECT * FROM users")
    Single<List<User>> listAll();

    @Query("SELECT follower.UserId as id, follower.UserName as name, follower.UserCreatedAt as createdAt, r.RelationshipStatus as status " +
            "FROM relationships as r " +
            "JOIN users as follower on r.RelationshipFollowerId = follower.UserId " +
            "WHERE r.RelationshipFollowedId = :userId")
    Flowable<List<Follower>> listFollowers(String userId);

    @TypeConverters(RelationshipStatusConverter.class)
    @Query("SELECT follower.UserId, follower.UserName, follower.UserCreatedAt FROM relationships as r " +
            "JOIN users as follower on r.RelationshipFollowerId = follower.UserId " +
            "WHERE r.RelationshipFollowedId = :userId AND r.RelationshipStatus = :status")
    Flowable<List<User>> listFollowerUsersByStatus(String userId, Relationship.Status status);
}
