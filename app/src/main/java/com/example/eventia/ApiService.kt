package com.example.eventia

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

data class LoginResponse(
    val id: Int,
    val name: String,
    val email: String,
    val role: String
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

data class UpdateUserRequest(
    val id: Int,
    val name: String,
    val email: String,
    val password: String?,
    val role: String?
)

interface ApiService {

    @POST("cadastro.php")
    fun registerUser(@Body request: RegisterRequest): Call<RegistrationResponse>

    @GET("login.php")
    fun loginUser(@Query("email") email: String, @Query("password") password: String): Call<List<LoginResponse>>

@GET("listar_usuarios.php")
    fun getUsuarios(): Call<List<LoginResponse>>

    @FormUrlEncoded
    @POST("excluir_usuario.php")
    fun excluirUsuario(@Field("id") id: Int): Call<RegistrationResponse>

    @POST("atualizar_usuario.php")
    fun updateUser(@Body request: UpdateUserRequest): Call<RegistrationResponse>

    @FormUrlEncoded
    @POST("criar_usuario.php")
    fun criarUsuario(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String
    ): Call<ResponseBody>

    @GET("listar_eventos.php")
    fun getEventos(): Call<List<Evento>>

    @FormUrlEncoded
    @POST("criar_evento.php")
    fun criarEvento(
        @Field("nome") nome: String,
        @Field("data") data: String,
        @Field("local") local: String,
        @Field("preco") preco: String,
        @Field("descricao") descricao: String,
        @Field("imagem_url") imagemUrl: String,
        @Field("id_usuario") idUsuario: Int
    ): Call<RegistrationResponse>

    @FormUrlEncoded
    @POST("atualizar_evento.php")
    fun atualizarEvento(
        @Field("id") id: Int,
        @Field("nome") nome: String,
        @Field("data") data: String,
        @Field("local") local: String,
        @Field("preco") preco: String,
        @Field("descricao") descricao: String,
        @Field("imagem_url") imagemUrl: String
    ): Call<RegistrationResponse>

    @FormUrlEncoded
    @POST("excluir_evento.php")
    fun excluirEvento(@Field("id") id: String): Call<RegistrationResponse>
}