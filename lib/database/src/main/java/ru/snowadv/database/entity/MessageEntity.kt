package ru.snowadv.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    @ColumnInfo(name = "message_id")
    val id: Long,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "sender_id")
    val senderId: Long,
    @ColumnInfo(name = "sender_full_name")
    val senderFullName: String,
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String? = null,
    @ColumnInfo(name = "reactions")
    val reactions: List<ReactionEntity>,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "subject")
    val subject: String = "", // will be empty string for DM messages
    @ColumnInfo(name = "stream_name")
    val streamName: String? = null, // will be null if it is DM message
    @ColumnInfo(name = "stream_id")
    val streamId: Long? = null, // will be null if it is DM message
    @ColumnInfo(name = "flags")
    val flags: List<String> = emptyList(),
)