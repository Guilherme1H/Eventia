package com.example.eventia

import com.example.eventia.Evento
interface IEventoRepository {

    fun toggleFavoriteStatus(evento: Evento)
}