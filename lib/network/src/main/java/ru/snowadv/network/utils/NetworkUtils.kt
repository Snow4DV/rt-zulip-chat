package ru.snowadv.network.utils

import kotlinx.serialization.json.Json
import retrofit2.HttpException
import ru.snowadv.model.Resource
import ru.snowadv.network.model.ErrorResponseDto

object NetworkUtils {
    private val json = Json { ignoreUnknownKeys = true }
    fun Throwable.isHttpExceptionWithCode(code: Int): Boolean {
        return this is HttpException && this.code() == code
    }

    fun Throwable.isHttpExceptionWithCode(serverCode: String): Boolean {
        (this as? HttpException)?.response()?.errorBody()?.string()?.let {
            return try {
                json.decodeFromString<ErrorResponseDto>(it).code == serverCode
            } catch (e: Exception) {
                false
            }
        }
        return false
    }

    fun Throwable.isHttpExceptionWithCodes(serverCodes: Set<String>): Boolean {
        (this as? HttpException)?.response()?.errorBody()?.string()?.let {
            return try {
                json.decodeFromString<ErrorResponseDto>(it).code in serverCodes
            } catch (e: Exception) {
                false
            }
        }
        return false
    }

    fun Throwable.getHttpExceptionCode(): String? {
        (this as? HttpException)?.response()?.errorBody()?.string()?.let {
            return try {
                json.decodeFromString<ErrorResponseDto>(it).code
            } catch (e: Throwable) {
                null
            }
        }
        return null
    }

    fun Throwable.getHttpExceptionMessage(): String? {
        (this as? HttpException)?.response()?.errorBody()?.string()?.let {
            return try {
                json.decodeFromString<ErrorResponseDto>(it).msg
            } catch (e: Throwable) {
                null
            }
        }
        return null
    }

    fun <T> Result<T>.toResourceWithErrorMessage(cachedData: T? = null): Resource<T> {
        return this.fold(
            onSuccess = {
                Resource.Success(it)
            },
            onFailure = {
                Resource.Error(it, cachedData, it.getHttpExceptionMessage())
            },
        )
    }
}