package ru.snowadv.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.snowadv.database.entity.TopicEntity

@Dao
interface TopicsDao {
    @Query("SELECT * FROM topics WHERE stream_id = :streamId")
    suspend fun getTopicsByStreamId(streamId: Long): List<TopicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(topicEntity: TopicEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(topics: List<TopicEntity>)

    @Transaction
    suspend fun insertTopicsIfChanged(streamId: Long, topics: List<TopicEntity>) {
        if (getTopicsByStreamId(streamId) != topics) {
            clearTopicsForStreamId(streamId)
            insertAll(topics)
        }
    }

    @Query("DELETE FROM topics WHERE stream_id = :streamId")
    suspend fun clearByStreamId(streamId: Long)

    @Query("DELETE FROM topics")
    suspend fun clear()

    @Query("DELETE FROM topics WHERE stream_id = :streamId")
    suspend fun clearTopicsForStreamId(streamId: Long)
}