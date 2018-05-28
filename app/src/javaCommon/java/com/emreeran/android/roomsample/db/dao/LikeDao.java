package com.emreeran.android.roomsample.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.emreeran.android.roomsample.db.entity.Like;

import java.util.List;

/**
 * Created by Emre Eran on 25.04.2018.
 */
@Dao
public interface LikeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Like like);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Like> likes);

    @Query("SELECT * FROM likes")
    List<Like> list();

    @Query("DELETE FROM likes")
    void purge();
}
