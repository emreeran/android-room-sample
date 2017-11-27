package com.emreeran.android.roomsample.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Emre Eran on 24.11.2017.
 */
@Entity(tableName = "users",
        primaryKeys = {"UserId"},
        indices = {
                @Index(value = "UserId", unique = true),
                @Index(value = "UserName", unique = true)
        }
)
public class User {
    @NonNull
    @ColumnInfo(name = "UserId")
    public final String id;

    @ColumnInfo(name = "UserName")
    public final String name;

    @NonNull
    @ColumnInfo(name = "UserCreatedAt")
    public final Date createdAt;

    public User(@NonNull String id, String name, @NonNull Date createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    @Ignore
    public User(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.createdAt = new Date();
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
