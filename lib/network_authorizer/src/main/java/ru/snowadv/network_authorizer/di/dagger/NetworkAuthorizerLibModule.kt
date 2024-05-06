package ru.snowadv.network_authorizer.di.dagger

import com.skydoves.retrofit.adapters.result.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.snowadv.network_authorizer.api.ZulipAuthApi

@Module
internal class NetworkAuthorizerLibModule {

    @Reusable
    @Provides
    fun provideZulipAuthApi(
        converterFactory: Converter.Factory,
        resultCallAdapterFactory: ResultCallAdapterFactory,
    ): ZulipAuthApi {
        return ZulipAuthApi(
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