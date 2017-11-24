package com.emreeran.android.roomsample.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.emreeran.android.roomsample.db.UserRelationshipStatusConverter;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Emre Eran on 24.11.2017.
 */
@Entity(tableName = "userRelationships",
        primaryKeys = "userRelationshipId",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userRelationshipFollowerId"),
                @ForeignKey(entity = User.class, parentColumns = "userId", childColumns = "userRelationshipFollowedId")
        },
        indices = {
                @Index(value = "userRelationshipFollowerId"),
                @Index(value = {"userRelationshipFollowerId", "userRelationshipFollowedId"}, unique = true, name = "userRelationshipFollowIndex")
        }
)
public class UserRelationship {
    @NonNull
    @ColumnInfo(name = "userRelationshipId")
    public String id;

    @ColumnInfo(name = "userRelationshipFollowerId")
    public String followerId;

    @ColumnInfo(name = "userRelationshipFollowedId")
    public String followedId;

    @TypeConverters(UserRelationshipStatusConverter.class)
    @ColumnInfo(name = "userRelationshipStatus")
    public Status status;

    @ColumnInfo(name = "userRelationshipCreatedAt")
    public Date createdAt;

    public UserRelationship(String id, String followerId, String followedId, Status status, Date createdAt) {
        this.id = id;
        this.followerId = followerId;
        this.followedId = followedId;
        this.status = status;
        this.createdAt = createdAt;
    }

    @Ignore
    public UserRelationship(String followerId, String followedId, Status status) {
        this.id = UUID.randomUUID().toString();
        this.followerId = followerId;
        this.followedId = followedId;
        this.status = status;
        this.createdAt = new Date();
    }

    public enum Status {
        ACCEPTED(0),
        PENDING(1);

        private int mCode;

        Status(int code) {
            mCode = code;
        }

        public int getCode() {
            return mCode;
        }
    }
}
