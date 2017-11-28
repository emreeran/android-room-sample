package com.emreeran.android.roomsample.vo;

import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.emreeran.android.roomsample.db.RelationshipStatusConverter;

import java.util.Date;

/**
 * Created by Emre Eran on 28.11.2017.
 */

public class Follower {
    @NonNull
    public String id;

    @NonNull
    public String relationshipId;

    public String name;

    @NonNull
    public Date createdAt;

    @TypeConverters(RelationshipStatusConverter.class)
    public Relationship.Status status;
}
