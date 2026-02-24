package org.example.project.book.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

object StringListTypeConverter {

    @TypeConverter
    fun fromStringToList(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromListToString(value: List<String>): String {
        return Json.encodeToString(value)
    }
}