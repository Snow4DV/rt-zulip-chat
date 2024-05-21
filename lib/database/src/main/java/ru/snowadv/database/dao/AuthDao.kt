package ru.snowadv.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ru.snowadv.database.entity.AuthUserEntity

@Dao
interface AuthDao {
    @Query("SELECT * FROM auth_users")
    suspend fun getCurrentAuth(): AuthUserEntity?

    @Query("DELETE FROM auth_users")
    suspend fun clearAuth()

    @Insert
    suspend fun insert(authUserEntity: AuthUserEntity)

    @Transaction
    suspend fun replaceAuthUser(userId: Long, email: String, apiKey: String) : AuthUserEntity {
        AuthUserEntity(id = userId, email = email, apiKey = apiKey).let { user ->
            return replaceAuthUser(user)
        }
    }

    @Transaction
    suspend fun replaceAuthUser(authUserEntity: AuthUserEntity) : AuthUserEntity {
        clearAuth()
        insert(authUserEntity)
        return authUserEntity
    }
}