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
        primaryKeys = "PostId",
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "UserId",
                        childColumns = "PostUserId"
                )
        },
        indices = {
                @Index(value = "PostUserId"),
                @Index(value = {"PostId", "PostUserId"}, unique = true, name = "PostUserIndex")
        }
)
public class Post {
    @NonNull
    @ColumnInfo(name = "PostId")
    public final String id;

    @ColumnInfo(name = "PostContent")
    public final String content;

    @NonNull
    @ColumnInfo(name = "PostUserId")
    public final String userId;

    @NonNull
    @ColumnInfo(name = "PostCreatedAt")
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
