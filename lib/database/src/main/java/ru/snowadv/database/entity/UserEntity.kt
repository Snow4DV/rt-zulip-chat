package ru.snowadv.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val id: Long,
    @ColumnInfo(name = "full_name")
    val fullName: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String?,
    @ColumnInfo(name = "timestamp")
    val timestampEpochSeconds: Long,
)