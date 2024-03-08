package ru.snowadv.app_contacts.domain.model

sealed class RequestResult<T> {
    class Error<T>: RequestResult<T>()
    class Success<T>(val data: T): RequestResult<T>()
}