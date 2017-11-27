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
@Entity(tableName = "likes",
        primaryKeys = "LikeId",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "UserId", childColumns = "LikeUserId"),
                @ForeignKey(entity = Post.class, parentColumns = "PostId", childColumns = "LikePostId")
        },
        indices = {
                @Index(value = "LikePostId"),
                @Index(value = {"LikeUserId", "LikePostId"}, unique = true, name = "LikeUserPostIndex")
        }
)

public class Like {
    @NonNull
    @ColumnInfo(name = "LikeId")
    public String id;

    @ColumnInfo(name = "LikeUserId")
    public String userId;

    @ColumnInfo(name = "LikePostId")
    public String postId;

    @NonNull
    @ColumnInfo(name = "LikeCreatedAt")
    public Date createdAt;

    public Like(@NonNull String id, String userId, String postId, @NonNull Date createdAt) {
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

    @Override
    public String toString() {
        return "Like{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", postId='" + postId + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
