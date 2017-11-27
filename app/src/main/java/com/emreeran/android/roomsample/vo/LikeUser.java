package com.emreeran.android.roomsample.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Emre Eran on 27.11.2017.
 */

public class LikeUser {
    @NonNull
    @ColumnInfo(name = "LikeId")
    public String id;

    @NonNull
    @ColumnInfo(name = "LikeCreatedAt")
    public Date createdAt;

    @Embedded(prefix = "Like")
    public User user;

    public LikeUser(@NonNull String id, @NonNull Date createdAt, User user) {
        this.id = id;
        this.createdAt = createdAt;
        this.user = user;
    }
}
