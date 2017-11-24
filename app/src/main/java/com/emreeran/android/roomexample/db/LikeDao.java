package com.emreeran.android.roomexample.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.emreeran.android.roomexample.vo.Like;

/**
 * Created by Emre Eran on 24.11.2017.
 */
@Dao
public interface LikeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Like like);
}
