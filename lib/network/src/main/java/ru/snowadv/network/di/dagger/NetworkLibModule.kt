package ru.snowadv.network.di.dagger

import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.interceptor.BadAuthResponseInterceptor
import ru.snowadv.network.interceptor.HeaderBasicAuthInterceptor
import ru.snowadv.network.interceptor.TimeoutSetterInterceptor

@Module
internal class NetworkLibModule {

    @Reusable
    @Provides
    fun provideZulipApi(
        headerBasicAuthInterceptor: HeaderBasicAuthInterceptor,
        timeoutSetterInterceptor: TimeoutSetterInterceptor,
        badAuthResponseInterceptor: BadAuthResponseInterceptor,
        converterFactory: Converter.Factory,
        resultCallAdapterFactory: ResultCallAdapterFactory,
    ): ZulipApi {
        return ZulipApi(
            headerBasicAuthInterceptor = headerBasicAuthInterceptor,
            timeoutSetterInterceptor = timeoutSetterInterceptor,
            badAuthResponseInterceptor = badAuthResponseInterceptor,
            converterFactory = converterFactory,
            resultCallAdapterFactory = resultCallAdapterFactory,
        )
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
}