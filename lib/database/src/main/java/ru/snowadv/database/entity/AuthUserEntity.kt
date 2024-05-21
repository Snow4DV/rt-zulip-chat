package ru.snowadv.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_users")
data class AuthUserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val id: Long,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "api_key")
    val apiKey: String,
)