package com.emreeran.android.roomsample.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Relation;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

/**
 * Created by Emre Eran on 27.11.2017.
 */

public class UserWithFollowers {
    @NonNull
    @ColumnInfo(name = "UserId")
    public String id;

    @ColumnInfo(name = "UserName")
    public String name;

    @NonNull
    @ColumnInfo(name = "UserCreatedAt")
    public Date createdAt;

    @Relation(entity = Relationship.class, parentColumn = "UserId", entityColumn = "RelationshipFollowedId")
    public List<Relationship> followers;

    @Override
    public String toString() {
        return "UserWithFollowers{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", followers=" + followers +
                '}';
    }
}
