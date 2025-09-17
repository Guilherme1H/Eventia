package com.example.eventia

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class LoginResponse(
    val id: Int,
    val name: String,
    val email: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class RegistrationResponse(
    val success: Boolean,
    val message: String
)


interface ApiService {
    @POST("cadastro.php")
    fun registerUser(@Body request: RegisterRequest): Call<RegistrationResponse>

    @GET("login.php")
    fun loginUser(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<List<LoginResponse>>
}
