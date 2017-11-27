package com.emreeran.android.roomsample.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.emreeran.android.roomsample.vo.Like;
import com.emreeran.android.roomsample.vo.Post;
import com.emreeran.android.roomsample.vo.Relationship;
import com.emreeran.android.roomsample.vo.User;

/**
 * Created by Emre Eran on 24.11.2017.
 */
@Database(version = 1, entities = {User.class, Post.class, Like.class, Relationship.class})
@TypeConverters(DateConverter.class)
public abstract class SampleDb extends RoomDatabase {
    private static volatile SampleDb INSTANCE;

    public static SampleDb getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SampleDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, SampleDb.class, "sample.db").build();
                }
            }
        }
        return INSTANCE;
    }

    abstract public UserDao userDao();

    abstract public PostDao postDao();

    abstract public LikeDao likeDao();

    abstract public RelationshipDao relationshipDao();
}
