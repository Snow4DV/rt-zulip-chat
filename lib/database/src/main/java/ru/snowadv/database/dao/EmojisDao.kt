package ru.snowadv.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.snowadv.database.entity.EmojiEntity
import ru.snowadv.database.entity.MessageEntity

@Dao
interface EmojisDao {
    @Query("SELECT * FROM emojis")
    suspend fun getEmojis(): List<EmojiEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmojis(emojis: List<EmojiEntity>)

    @Transaction
    suspend fun updateEmojisIfChanged(emojis: List<EmojiEntity>) {
        if (emojis != getEmojis()) {
            clear()
            insertEmojis(emojis)
        }
    }

    @Query("DELETE FROM emojis")
    suspend fun clear()
}