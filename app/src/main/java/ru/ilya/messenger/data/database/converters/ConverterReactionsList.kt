package ru.ilya.messenger.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.ilya.messenger.domain.entities.Reactions

class ConverterReactionsList {

    private val gson = Gson()

    @TypeConverter
    fun listToString(data: List<Reactions>?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    fun stringToList(topicDbModel: String): List<Reactions>? {
        val listType = object : TypeToken<List<Reactions>?>() {}.type
        return gson.fromJson<List<Reactions>?>(topicDbModel, listType)
    }

}