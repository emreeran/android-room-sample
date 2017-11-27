package com.emreeran.android.roomsample.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.emreeran.android.roomsample.vo.Relationship;

/**
 * Created by Emre Eran on 24.11.2017.
 */
@Dao
public interface RelationshipDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Relationship relationship);

    @Delete
    void delete(Relationship relationship);
}
