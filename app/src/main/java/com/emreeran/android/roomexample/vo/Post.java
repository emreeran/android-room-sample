package com.emreeran.android.roomexample.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Emre Eran on 24.11.2017.
 */
@Entity(tableName = "posts",
        primaryKeys = "postId",
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "postUserId")},
        indices = {@Index(value = "postUserId")}
)
public class Post {
    @NonNull
    @ColumnInfo(name = "postId")
    public final String id;

    @ColumnInfo(name = "postContent")
    public final String content;

    @ColumnInfo(name = "postUserId")
    public final String userId;

    @ColumnInfo(name = "postCreatedAt")
    public final Date createdAt;

    public Post(String id, String content, String userId, Date createdAt) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    @Ignore
    public Post(String content, String userId) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.userId = userId;
        this.createdAt = new Date();
    }
}
