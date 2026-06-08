package com.mobile.felix.musicapp.core.domain

sealed interface Failure {
    object NetworkError : Failure
    object Unknown : Failure
}