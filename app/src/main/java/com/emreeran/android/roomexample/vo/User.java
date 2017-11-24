package com.emreeran.android.roomexample.vo;

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
        primaryKeys = {"userId"},
        indices = {@Index(value = "userId", unique = true)}
)
public class User {
    @NonNull
    @ColumnInfo(name = "userId")
    public final String id;

    @ColumnInfo(name = "userName")
    public final String name;

    @ColumnInfo(name = "userCreatedAt")
    public final Date createdAt;

    public User(String id, String name, Date createdAt) {
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
}
