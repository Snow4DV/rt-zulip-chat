package ru.snowadv.chatapp.model

internal data class ErrorResponseDto(val msg: String, val code: String = "BAD_REQUEST", val result: String = "error")