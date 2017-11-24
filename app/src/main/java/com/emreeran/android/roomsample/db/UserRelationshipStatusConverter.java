package com.emreeran.android.roomsample.db;

import android.arch.persistence.room.TypeConverter;

import com.emreeran.android.roomsample.vo.UserRelationship;

/**
 * Created by Emre Eran on 24.11.2017.
 */

public class UserRelationshipStatusConverter {
    @TypeConverter
    public static UserRelationship.Status toStatus(Integer status) {
        if (status == UserRelationship.Status.ACCEPTED.getCode()) {
            return UserRelationship.Status.ACCEPTED;
        } else if (status == UserRelationship.Status.PENDING.getCode()) {
            return UserRelationship.Status.PENDING;
        }
        throw new IllegalArgumentException("Unrecognized user relationship status");
    }

    @TypeConverter
    public static Integer toInteger(UserRelationship.Status status) {
        return status.getCode();
    }
}
