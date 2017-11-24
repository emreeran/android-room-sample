package com.emreeran.android.roomexample.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.emreeran.android.roomexample.vo.Like;
import com.emreeran.android.roomexample.vo.Post;
import com.emreeran.android.roomexample.vo.User;
import com.emreeran.android.roomexample.vo.UserRelationship;

/**
 * Created by Emre Eran on 24.11.2017.
 */
@Database(version = 1, entities = {User.class, Post.class, Like.class, UserRelationship.class})
@TypeConverters(DateConverter.class)
public abstract class SampleDb extends RoomDatabase {
    abstract public UserDao userDao();

    abstract public PostDao postDao();

    abstract public LikeDao likeDao();

    abstract public UserRelationshipDao userRelationship();
}
