package ru.snowadv.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.snowadv.database.entity.UserEntity

@Dao
interface UsersDao {
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM users WHERE user_id = :userId")
    suspend fun getUser(userId: Long): UserEntity?

    @Query("DELETE FROM users")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userEntity: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)
}