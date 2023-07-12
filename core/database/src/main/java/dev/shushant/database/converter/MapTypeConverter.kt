package dev.shushant.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MapTypeConverter {

    @TypeConverter
    fun fromMap(map: Map<String, String>): String {
        return Json.encodeToString(map)
    }

    @TypeConverter
    fun toMap(json: String): Map<String, String> {
        return Json.decodeFromString(json)
    }

    @TypeConverter
    fun fromExchangeRateMap(map: Map<String, Double>): String {
        return Json.encodeToString(map)
    }

    @TypeConverter
    fun toExchangeRateMap(json: String): Map<String, Double> {
        return Json.decodeFromString(json)
    }
}