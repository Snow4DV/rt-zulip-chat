package ru.snowadv.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streams")
data class StreamEntity(
    @PrimaryKey
    @ColumnInfo(name = "stream_id")
    val id: Long,
    @ColumnInfo(name = "stream_name")
    val name: String,
    @ColumnInfo(name = "subscribed")
    val subscribed: Boolean,
    @ColumnInfo(name = "color")
    val color: String?,
)