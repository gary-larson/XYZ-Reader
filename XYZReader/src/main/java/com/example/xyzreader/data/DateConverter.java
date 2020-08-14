package com.example.xyzreader.data;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Class to convert date objects to and from timestamp(long) for room
 */
public class DateConverter {
    /**
     * Method to convert timestamp(long) to date
     * @param timestamp to be converted
     * @return timestamp(long)
     */
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    /**
     * Method to convert date to timestamp(long)
     * @param date to be converted
     * @return date
     */
    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
