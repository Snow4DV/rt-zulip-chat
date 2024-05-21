package ru.snowadv.database.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import dagger.Reusable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.snowadv.database.entity.ReactionEntity
import ru.snowadv.database.entity.UserEntity
import javax.inject.Inject

@ProvidedTypeConverter
@Reusable
internal class CacheDatabaseTypeConverter @Inject constructor(
    private val json: Json,
) {
    @TypeConverter
    fun stringListToJson(list: List<String>): String {
        return json.encodeToString(serializer(),  list)
    }

    @TypeConverter
    fun stringListFromJson(jsonStr: String): List<String> {
        return json.decodeFromString(jsonStr)
    }


    @TypeConverter
    fun reactionsToJson(reactions: List<ReactionEntity>): String {
        return json.encodeToString(serializer(), reactions)
    }

    @TypeConverter
    fun reactionsFromJson(jsonStr: String): List<ReactionEntity> {
        return json.decodeFromString(jsonStr)
    }
}