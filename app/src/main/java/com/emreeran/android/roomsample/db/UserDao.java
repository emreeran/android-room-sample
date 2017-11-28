package com.emreeran.android.roomsample.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.emreeran.android.roomsample.vo.User;

import java.util.List;

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
}
