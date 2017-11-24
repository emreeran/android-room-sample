package com.emreeran.android.roomexample.db;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Emre Eran on 24.11.2017.
 */

public class DateConverter {
    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }
}
