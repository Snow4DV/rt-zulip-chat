package ru.snowadv.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.snowadv.database.entity.StreamEntity

@Dao
interface StreamsDao {
    @Query("SELECT * FROM streams")
    suspend fun getAll(): List<StreamEntity>

    @Query("SELECT * FROM streams WHERE subscribed = 1")
    suspend fun getSubscribed(): List<StreamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(streamEntity: StreamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(streams: List<StreamEntity>)

    @Transaction
    suspend fun insertAllStreams(streams: List<StreamEntity>) {
        val subscribedIds = getSubscribed().asSequence().map { it.id }.toSet()
        clear()
        insertAll(streams.map { it.copy(subscribed = it.id in subscribedIds) })
    }

    @Transaction
    suspend fun insertSubscribedStreams(streams: List<StreamEntity>) {
        insertAll(streams)
    }

    @Query("DELETE FROM streams")
    suspend fun clear()

    @Transaction
    suspend fun insertNewStream(streamId: Long, streamName: String) {
        insert(StreamEntity(id = streamId, name = streamName, subscribed = false))
    }

    @Transaction
    suspend fun insertNewSubscribed(streamId: Long, streamName: String) {
        insert(StreamEntity(id = streamId, name = streamName, subscribed = true))
    }

    @Query("DELETE FROM streams WHERE stream_id = :streamId")
    suspend fun removeStreamById(streamId: Long)
}