package com.codeadd.gitresearch.utils

//Lets us perform safe api call with error handling
sealed class Result<out T: Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: String) : Result<Nothing>()
}