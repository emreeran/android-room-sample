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
@Entity(tableName = "likes",
        primaryKeys = "likeId",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "likeUserId"),
                @ForeignKey(entity = Post.class, parentColumns = "postId", childColumns = "likePostId")
        },
        indices = {
                @Index(value = "likePostId"),
                @Index(value = {"likeUserId", "likePostId"}, unique = true, name = "likeUserPostIndex")
        }
)
public class Like {
    @NonNull
    @ColumnInfo(name = "likeId")
    public String id;

    @ColumnInfo(name = "likeUserId")
    public String userId;

    @ColumnInfo(name = "likePostId")
    public String postId;

    @ColumnInfo(name = "likeCreatedAt")
    public Date createdAt;

    public Like(String id, String userId, String postId, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.createdAt = createdAt;
    }

    @Ignore
    public Like(String userId, String postId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.postId = postId;
        this.createdAt = new Date();
    }
}
