package com.example.data

import androidx.room.TypeConverter

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
class Converters {

    /**
     * Converts a comma-separated String to a List of Strings.
     */
    @TypeConverter
    fun fromStringToList(string: String): List<String> {
        return string.split(",")
    }

    /**
     * Converts a List of Strings to a comma-separated String.
     */
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return list.joinToString(",")
    }

    /**
     * Converts a comma-separated String to a List of Ints.
     */
    @TypeConverter
    fun toIntList(value: String): List<Int> {
        return value.split(",").map { it.toInt() }
    }

    /**
     * Converts a List of Ints to a comma-separated String.
     */
    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        return value.joinToString(separator = ",")
    }

}