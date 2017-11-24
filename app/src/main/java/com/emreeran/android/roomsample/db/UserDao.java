package com.emreeran.android.roomsample.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import com.emreeran.android.roomsample.vo.User;

/**
 * Created by Emre Eran on 24.11.2017.
 */
@Dao
public interface UserDao {
    @Insert
    void insert(User user);
}
