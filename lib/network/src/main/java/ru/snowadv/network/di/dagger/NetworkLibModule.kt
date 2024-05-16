package ru.snowadv.network.di.dagger

import android.content.pm.ApplicationInfo
import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import ru.snowadv.model.BaseUrlProvider
import ru.snowadv.network.api.LoggerToggle
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.interceptor.BadAuthResponseInterceptor
import ru.snowadv.network.interceptor.HeaderBasicAuthInterceptor
import ru.snowadv.network.interceptor.TimeoutSetterInterceptor

@Module
internal class NetworkLibModule {

    @Reusable
    @Provides
    fun provideZulipApi(
        retrofit: Retrofit,
    ): ZulipApi {
        return retrofit.create()
    }

    @Reusable
    @Provides
    fun provideConverterFactory(json: Json): Converter.Factory {
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Reusable
    @Provides
    fun provideResultCallFactory(): ResultCallAdapterFactory {
        return ResultCallAdapterFactory.create()
    }

    @Reusable
    @Provides
    fun provideOkHttpClient(
        headerBasicAuthInterceptor: HeaderBasicAuthInterceptor,
        timeoutSetterInterceptor: TimeoutSetterInterceptor,
        badAuthResponseInterceptor: BadAuthResponseInterceptor,
        loggerToggle: LoggerToggle,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(headerBasicAuthInterceptor)
            .addInterceptor(timeoutSetterInterceptor)
            .addInterceptor(badAuthResponseInterceptor)
            .let {
                if (loggerToggle.isLoggingEnabled) {
                    it.addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                } else {
                    it
                }
            }.build()
    }

    @Reusable
    @Provides
    fun provideRetrofit(
        converterFactory: Converter.Factory,
        resultCallAdapterFactory: ResultCallAdapterFactory,
        okHttpClient: OkHttpClient,
        baseUrlProvider: BaseUrlProvider,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrlProvider.apiUrl)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(resultCallAdapterFactory)
            .client(okHttpClient)
            .build()
    }
}