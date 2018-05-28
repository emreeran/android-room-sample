package com.emreeran.android.roomsample.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by Emre Eran on 25.04.2018.
 */
@Entity(
        tableName = "likes",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "userId"),
                @ForeignKey(entity = Post.class, parentColumns = "id", childColumns = "postId")
        },
        indices = {
                @Index(value = "postId", name = "LikeUserIndex"),
                @Index(value = {"userId", "postId"}, name = "UserPostIndex", unique = true)
        }
)
public class Like {
    @PrimaryKey(autoGenerate = true)
    public final int id;
    public final int userId;
    public final int postId;
    public final Date createdAt;

    @Ignore
    public Like(int userId, int postId) {
        this.id = 0;
        this.userId = userId;
        this.postId = postId;
        this.createdAt = new Date();
    }

    public Like(int id, int userId, int postId, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", userId=" + userId +
                ", postId=" + postId +
                ", createdAt=" + createdAt +
                '}';
    }
}
