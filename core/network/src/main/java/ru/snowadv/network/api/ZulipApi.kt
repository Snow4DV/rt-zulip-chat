package ru.snowadv.network.api

import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.network.interceptor.HeaderBasicAuthInterceptor
import ru.snowadv.network.model.AllStreamsDto
import ru.snowadv.network.model.AllUsersDto
import ru.snowadv.network.model.AllUsersPresenceDto
import ru.snowadv.network.model.EmojisDto
import ru.snowadv.network.model.EventQueueDto
import ru.snowadv.network.model.EventsDto
import ru.snowadv.network.model.MessagesDto
import ru.snowadv.network.model.NarrowDto
import ru.snowadv.network.model.SingleUserDto
import ru.snowadv.network.model.SingleUserPresenceDto
import ru.snowadv.network.model.SubscribedStreamsDto
import ru.snowadv.network.model.TopicsDto

interface ZulipApi { // Will add annotations later, at this moment it is used with stubs only
    // Streams
    @GET("streams")
    suspend fun getAllStreams(): Result<AllStreamsDto>

    @GET("users/me/subscriptions")
    suspend fun getSubscribedStreams(): Result<SubscribedStreamsDto>

    // Topics
    @GET("users/me/{stream_id}/topics")
    suspend fun getTopicsByChannel(@Path(value = "stream_id") streamId: Long): Result<TopicsDto>

    // Users
    @GET("users")
    suspend fun getAllUsers(): Result<AllUsersDto>

    @GET("users/{user_id}")
    suspend fun getUser(@Path(value = "user_id") userId: Long): Result<SingleUserDto>

    @GET("users/me")
    suspend fun getCurrentUser(): Result<SingleUserDto>

    // Users presence
    @GET("realm/presence")
    suspend fun getAllUsersPresence(): Result<AllUsersPresenceDto>

    @GET("users/{user_id}/presence")
    suspend fun getUserPresence(@Path(value = "user_id") userId: Long): Result<SingleUserPresenceDto>

    // Messages
    @GET("messages")
    suspend fun getMessages(
        @Query(value = "anchor")
        anchor: String = "newest",
        @Query(value = "num_before")
        numBefore: Int,
        @Query(value = "num_after")
        numAfter: Int = 0,
        @Query(value = "narrow")
        narrow: List<NarrowDto>
    ): Result<MessagesDto>

    @FormUrlEncoded
    @POST("messages")
    suspend fun sendMessage(stream: String, topic: String, text: String): Result<Unit>

    @FormUrlEncoded
    @PATCH("messages/{msg_id}")
    fun editMessage(
        @Path("msg_id")
        messageId: Long,
        @Field("content")
        content: String,
    ): Result<Unit>

    @DELETE("messages/{msg_id}")
    fun deleteMessage(
        @Path("msg_id")
        messageId: Long
    ): Result<Unit>

    @FormUrlEncoded
    @POST("messages/{message_id}/reactions")
    suspend fun addReaction(
        @Path("message_id")
        messageId: Long,
        @Field("emoji_name")
        emojiName: String
    ): Result<Unit>

    @DELETE("messages/{message_id}/reactions")
    suspend fun removeReaction(
        @Path("message_id")
        messageId: Long,
        @Query("emoji_name")
        emojiName: String,
    ): Result<Unit>

    // Emojis
    @GET("/static/generated/emoji/emoji_codes.json")
    suspend fun getEmojis(): Result<EmojisDto>

    // Event API
    /**
     * Event types: message (messages), subscription (changes in your subscriptions),
     * realm_user (changes to users in the organization and their properties, such as their name)
     *
     * Narrow example: "narrow": [["is", "dm"], ["stream", "Denmark"]]
     *
     * see https://zulip.com/api/construct-narrow
     */
    @POST("register")
    suspend fun registerEventQueue(
        @Query("event_types") eventTypes: List<String>,
        @Query("narrow") narrow: List<List<String>>,
    ): Result<EventQueueDto>

    @GET("events")
    suspend fun getEventsFromEventQueue(
        @Query("queue_id") queueId: String,
        @Query("last_event_id") lastEventId: Long,
    ): Result<EventsDto>

    companion object {
        const val BASE_URL = "https://tinkoff-android-spring-2024.zulipchat.com/api/v1/"
    }
}

fun ZulipApi(
    baseUrl: String = ZulipApi.BASE_URL,
    authProvider: AuthProvider,
    okHttpClient: OkHttpClient? = null,
    json: Json = defaultJson(),
): ZulipApi {
    return retrofit(baseUrl, authProvider, okHttpClient, json).create()
}

fun ZulipApi(authProvider: AuthProvider): ZulipApi {
    return retrofit(authProvider = authProvider).create()
}

private fun retrofit(
    baseUrl: String = ZulipApi.BASE_URL,
    authProvider: AuthProvider,
    okHttpClient: OkHttpClient? = null,
    json: Json = defaultJson(),
): Retrofit {

    val newOkhttpClient = (okHttpClient?.newBuilder() ?: OkHttpClient.Builder())
        .addInterceptor(HeaderBasicAuthInterceptor(authProvider))
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .addCallAdapterFactory(ResultCallAdapterFactory.create())
        .client(newOkhttpClient)
        .build()
}

private fun defaultJson(): Json {
    return Json { ignoreUnknownKeys = true }
}