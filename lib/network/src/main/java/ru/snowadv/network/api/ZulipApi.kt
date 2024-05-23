package ru.snowadv.network.api

import okhttp3.MultipartBody
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import ru.snowadv.model.InputStreamOpener
import ru.snowadv.network.file.InputStreamRequestBody
import ru.snowadv.network.interceptor.TimeoutSetterInterceptor.Companion.READ_TIMEOUT_HEADER
import ru.snowadv.network.model.AllStreamsResponseDto
import ru.snowadv.network.model.AllUsersResponseDto
import ru.snowadv.network.model.AllUsersPresenceDto
import ru.snowadv.network.model.EmojisResponseDto
import ru.snowadv.network.model.EventQueueResponseDto
import ru.snowadv.network.model.EventTypesRequestDto
import ru.snowadv.network.model.EventsResponseDto
import ru.snowadv.network.model.MessagesResponseDto
import ru.snowadv.network.model.Narrow2DArrayRequestDto
import ru.snowadv.network.model.NarrowListRequestDto
import ru.snowadv.network.model.SingleUserResponseDto
import ru.snowadv.network.model.SingleUserPresenceDto
import ru.snowadv.network.model.SubscribedStreamsResponseDto
import ru.snowadv.network.model.SubscriptionDetailsListRequestDto
import ru.snowadv.network.model.TopicsResponseDto
import ru.snowadv.network.model.UnsubscribeStreamsListRequestDto
import ru.snowadv.network.model.UploadFileResponseDto
import java.util.UUID

interface ZulipApi {

    companion object {
        const val DEFAULT_MESSAGE_TYPE = "stream"
        const val DEFAULT_MESSAGE_ANCHOR = "newest"
    }

    // Streams
    @GET("streams")
    suspend fun getAllStreams(): Result<AllStreamsResponseDto>

    @GET("users/me/subscriptions")
    suspend fun getSubscribedStreams(): Result<SubscribedStreamsResponseDto>

    // Topics
    @GET("users/me/{stream_id}/topics")
    suspend fun getTopicsByChannel(@Path(value = "stream_id") streamId: Long): Result<TopicsResponseDto>

    // Users
    @GET("users")
    suspend fun getAllUsers(): Result<AllUsersResponseDto>

    @GET("users/{user_id}")
    suspend fun getUser(@Path(value = "user_id") userId: Long): Result<SingleUserResponseDto>

    @GET("users/me")
    suspend fun getCurrentUser(): Result<SingleUserResponseDto>

    // Users presence
    @GET("realm/presence")
    suspend fun getAllUsersPresence(): Result<AllUsersPresenceDto>

    @GET("users/{user_id}/presence")
    suspend fun getUserPresence(@Path(value = "user_id") userId: Long): Result<SingleUserPresenceDto>

    // Messages
    @GET("messages")
    suspend fun getMessages(
        @Query(value = "anchor")
        anchor: String = DEFAULT_MESSAGE_ANCHOR,
        @Query(value = "num_before")
        numBefore: Int,
        @Query(value = "num_after")
        numAfter: Int,
        @Query(value = "narrow")
        narrow: NarrowListRequestDto,
        @Query(value = "apply_markdown")
        applyMarkdown: Boolean,
    ): Result<MessagesResponseDto>

    @Multipart
    @POST("user_uploads")
    suspend fun uploadFile(
        @Part
        file: MultipartBody.Part
    ): Result<UploadFileResponseDto>

    @POST("messages")
    suspend fun sendMessage(
        @Query("type") type: String = DEFAULT_MESSAGE_TYPE,
        @Query("to") stream: String,
        @Query("topic") topic: String,
        @Query("content") content: String
    ): Result<Unit>

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
    suspend fun getEmojis(): Result<EmojisResponseDto>

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
        @Query("event_types") eventTypes: EventTypesRequestDto,
        @Query("narrow") narrow: Narrow2DArrayRequestDto,
    ): Result<EventQueueResponseDto>

    @GET("events")
    suspend fun getEventsFromEventQueue(
        @Query("queue_id") queueId: String,
        @Query("last_event_id") lastEventId: Long,
        @Header(READ_TIMEOUT_HEADER) readTimeout: Long,
    ): Result<EventsResponseDto>

    @POST("users/me/subscriptions")
    suspend fun subscribeOrCreateStream(
        @Query("subscriptions") subscriptionsDetails: SubscriptionDetailsListRequestDto,
        @Query("history_public_to_subscribers") showHistoryToNewSubscribers: Boolean? = null,
        @Query("announce") announceChannel: Boolean? = null, // channel will be announced in "new channels" if true
    ): Result<Unit>

    @DELETE("users/me/subscriptions")
    suspend fun unsubscribeFromStreams(
        @Query("subscriptions") subscriptions: UnsubscribeStreamsListRequestDto,
    ): Result<Unit>
}

suspend fun ZulipApi.uploadFile(
    mimeType: String?,
    inputStreamOpener: InputStreamOpener,
    extension: String?,
): Result<UploadFileResponseDto> {

    val fileName = if (!extension.isNullOrBlank()) {
        "${UUID.randomUUID()}.${extension.trim().replace(" ", "")}"
    } else {
        UUID.randomUUID().toString()
    }

    return uploadFile(
        file = MultipartBody.Part.createFormData(
            name = "file",
            filename = fileName,
            body = InputStreamRequestBody(mimeType, inputStreamOpener),
        )
    )
}