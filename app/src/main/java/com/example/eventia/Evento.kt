package com.example.eventia

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Evento(
    val id: String = "",
    val nome: String = "",
    val data: String = "",
    val local: String = "",
    val imagemUrl: String = "",
    val descricao: String = "",
    val preco: Double = 0.0,
    var isFavorito: Boolean = false
) : Parcelable