package com.emreeran.android.roomsample.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.emreeran.android.roomsample.db.RelationshipStatusConverter;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Emre Eran on 24.11.2017.
 */
@Entity(tableName = "relationships",
        primaryKeys = "RelationshipId",
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "UserId", childColumns = "RelationshipFollowerId"),
                @ForeignKey(entity = User.class, parentColumns = "UserId", childColumns = "RelationshipFollowedId")
        },
        indices = {
                @Index(value = "RelationshipFollowerId"),
                @Index(value = "RelationshipFollowedId"),
                @Index(value = {"RelationshipFollowerId", "RelationshipFollowedId"}, unique = true, name = "RelationshipFollowIndex")
        }
)
public class Relationship {
    @NonNull
    @ColumnInfo(name = "RelationshipId")
    public String id;

    @NonNull
    @ColumnInfo(name = "RelationshipFollowerId")
    public String followerId;

    @NonNull
    @ColumnInfo(name = "RelationshipFollowedId")
    public String followedId;

    @TypeConverters(RelationshipStatusConverter.class)
    @ColumnInfo(name = "RelationshipStatus")
    public Status status;

    @NonNull
    @ColumnInfo(name = "RelationshipCreatedAt")
    public Date createdAt;

    public Relationship(@NonNull String id, @NonNull String followerId, @NonNull String followedId, Status status, @NonNull Date
            createdAt) {
        this.id = id;
        this.followerId = followerId;
        this.followedId = followedId;
        this.status = status;
        this.createdAt = createdAt;
    }

    @Ignore
    public Relationship(@NonNull String followerId, @NonNull String followedId, Status status) {
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

        @Override
        public String toString() {
            switch (mCode) {
                case 0:
                    return "Accepted";
                case 1:
                    return "Pending";
            }
            return "";
        }
    }
}
