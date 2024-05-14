package ru.snowadv.network.utils

import kotlinx.serialization.json.Json
import retrofit2.HttpException
import ru.snowadv.network.model.ErrorResponseDto

object NetworkUtils {
    private val json = Json { ignoreUnknownKeys = true }
    fun Throwable.isHttpExceptionWithCode(code: Int): Boolean {
        return this is HttpException && this.code() == code
    }

    fun Throwable.isHttpExceptionWithCode(serverCode: String): Boolean? {
        (this as? HttpException)?.response()?.errorBody()?.string()?.let {
            return try {
                json.decodeFromString<ErrorResponseDto>(it).code == serverCode
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
            } catch (e: Exception) {
                null
            }
        }
        return null
    }
}