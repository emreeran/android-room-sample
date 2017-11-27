package com.emreeran.android.roomsample.db;

import android.arch.persistence.room.TypeConverter;

import com.emreeran.android.roomsample.vo.Relationship;

/**
 * Created by Emre Eran on 24.11.2017.
 */

public class RelationshipStatusConverter {
    @TypeConverter
    public static Relationship.Status toStatus(Integer status) {
        if (status == Relationship.Status.ACCEPTED.getCode()) {
            return Relationship.Status.ACCEPTED;
        } else if (status == Relationship.Status.PENDING.getCode()) {
            return Relationship.Status.PENDING;
        }
        throw new IllegalArgumentException("Unrecognized user relationship status");
    }

    @TypeConverter
    public static Integer toInteger(Relationship.Status status) {
        return status.getCode();
    }
}
