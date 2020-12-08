package com.codeadd.gitresearch.network.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.codeadd.gitresearch.utils.Result

object RetrofitObject {
    private const val BASE_URL = "https://api.github.com/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build() //Doesn't require the adapter
    }

    //Creating only one instance of retrofit for whole Application Lifecycle
    val apiService: ApiService = getRetrofit().create(ApiService::class.java)

    //Generic function so that we could reuse it, handle errors
    suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Result<T> {
        return try {
            val myResp = call.invoke()
            if (myResp.isSuccessful) {
                Result.Success(myResp.body()!!)
            }
            else {
                Result.Error( "Check internet connection")
            }
        } catch (e: Exception) {
            Result.Error("Check internet connection")
        }
    }
}