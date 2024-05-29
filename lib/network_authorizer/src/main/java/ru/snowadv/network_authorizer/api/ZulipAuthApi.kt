package ru.snowadv.network_authorizer.api

import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.POST
import retrofit2.http.Query
import ru.snowadv.network_authorizer.model.AuthResponseDto

interface ZulipAuthApi {
    // Authorization
    @POST("fetch_api_key")
    suspend fun authorize(
        @Query(value = "username")
        username: String,
        @Query(value = "password")
        password: String,
    ): Result<AuthResponseDto>
}