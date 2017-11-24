package com.emreeran.android.roomexample.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.emreeran.android.roomexample.vo.Post;

/**
 * Created by Emre Eran on 24.11.2017.
 */
@Dao
public interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);
}
