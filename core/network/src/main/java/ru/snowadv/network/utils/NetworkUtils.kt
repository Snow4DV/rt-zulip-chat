package ru.snowadv.network.utils

import retrofit2.HttpException

object NetworkUtils {
    fun Throwable.isHttpExceptionWithCode(code: Int): Boolean {
        return this is HttpException && this.code() == code
    }
}