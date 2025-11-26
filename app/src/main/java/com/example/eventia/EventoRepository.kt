package com.example.eventia

import com.example.eventia.Evento

class EventoRepository : IEventoRepository {

    override fun toggleFavoriteStatus(evento: Evento) {

        evento.isFavorito = !evento.isFavorito
    }
}