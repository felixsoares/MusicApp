package com.mobile.felix.musicapp.core.domain

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val failure: Failure) : Result<Nothing>
}