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


    companion object {
        const val BASE_URL = "https://tinkoff-android-spring-2024.zulipchat.com/api/v1/"
    }
}


internal fun ZulipAuthApi (
    converterFactory: Converter.Factory,
    resultCallAdapterFactory: ResultCallAdapterFactory,
): ZulipAuthApi {
    return retrofit(
        converterFactory = converterFactory,
        resultCallAdapterFactory = resultCallAdapterFactory,
    ).create()
}

private fun retrofit(
    baseUrl: String = ZulipAuthApi.BASE_URL,
    okHttpClient: OkHttpClient = OkHttpClient.Builder().build(),
    converterFactory: Converter.Factory,
    resultCallAdapterFactory: ResultCallAdapterFactory,
): Retrofit {

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(resultCallAdapterFactory)
        .client(okHttpClient)
        .build()
}