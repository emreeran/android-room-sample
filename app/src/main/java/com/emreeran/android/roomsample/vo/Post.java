package com.emreeran.android.roomsample.vo;

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
        indices = {
                @Index(value = "postUserId"),
                @Index(value = {"postId", "postUserId"}, unique = true, name = "postUserIndex")
        }
)
public class Post {
    @NonNull
    @ColumnInfo(name = "postId")
    public final String id;

    @ColumnInfo(name = "postContent")
    public final String content;

    @NonNull
    @ColumnInfo(name = "postUserId")
    public final String userId;

    @NonNull
    @ColumnInfo(name = "postCreatedAt")
    public final Date createdAt;

    public Post(@NonNull String id, String content, @NonNull String userId, @NonNull Date createdAt) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    @Ignore
    public Post(String content, @NonNull String userId) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.userId = userId;
        this.createdAt = new Date();
    }
}
