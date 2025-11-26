package com.example.eventia

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Evento(
    val id: Int,
    val nome: String,
    val data: String,
    val local: String,
    val preco: Double,
    val descricao: String,
    val categoria: String?,
    @SerializedName("imagemUrl")
    val imagemUrl: String?,
    @SerializedName("id_usuario")
    val idUsuario: Int,
    var isFavorito: Boolean = false
) : Parcelable