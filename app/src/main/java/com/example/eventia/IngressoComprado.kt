package com.example.eventia

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IngressoComprado(
    val id: String,
    val eventoId: Int,
    val nomeEvento: String,
    val dataEvento: String,
    val localEvento: String,
    val imagemUrl: String?,
    val quantidade: Int
) : Parcelable