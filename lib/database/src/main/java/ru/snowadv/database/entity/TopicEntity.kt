package ru.snowadv.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey
    @ColumnInfo(name = "topic_uid")
    val uniqueId: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "stream_id")
    val streamId: Long
)