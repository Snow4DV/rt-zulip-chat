package ru.snowadv.auth_api.domain.model

sealed interface AuthorizationResult {
    data object Loading : AuthorizationResult
    data object InvalidEmail : AuthorizationResult
    data object InvalidPassword : AuthorizationResult
    data object BadConnection : AuthorizationResult
    data object BadAuth : AuthorizationResult
    data object Success : AuthorizationResult
}