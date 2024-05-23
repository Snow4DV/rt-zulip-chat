package ru.snowadv.network_authorizer.di.dagger

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
import ru.snowadv.model.LoggerToggle
import ru.snowadv.network_authorizer.api.ZulipAuthApi
import dagger.Lazy

@Module
internal class NetworkAuthorizerLibModule {

    @Reusable
    @Provides
    fun provideZulipAuthApi(
        retrofit: Retrofit,
    ): ZulipAuthApi {
        return retrofit.create()
    }

    @Reusable
    @Provides
    fun provideRetrofit(
        baseUrlProvider: BaseUrlProvider,
        converterFactory: Converter.Factory,
        resultCallAdapterFactory: ResultCallAdapterFactory,
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrlProvider.apiUrl)
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(resultCallAdapterFactory)
            .client(okHttpClient)
            .build()
    }

    @Reusable
    @Provides
    fun provideConverterFactory(json: Json): Converter.Factory {
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Reusable
    @Provides
    fun provideHttpLoggerInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Reusable
    @Provides
    fun provideOkHttpClient(loggerToggle: LoggerToggle, interceptor: Lazy<HttpLoggingInterceptor>): OkHttpClient {
        return OkHttpClient.Builder()
            .let {
            if (loggerToggle.isLoggingEnabled) {
                it.addNetworkInterceptor(interceptor.get())
            } else {
                it
            }
        }.build()
    }

    @Reusable
    @Provides
    fun provideResultCallFactory(): ResultCallAdapterFactory {
        return ResultCallAdapterFactory.create()
    }
}