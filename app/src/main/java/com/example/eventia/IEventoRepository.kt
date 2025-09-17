package com.example.eventia

import com.example.eventia.Evento

// A interface define O QUE um repositório de eventos DEVE ser capaz de fazer.
interface IEventoRepository {
    // No futuro, podemos ter uma função para buscar eventos do Firebase aqui.
    // suspend fun getEventos(): List<Evento>

    // Por enquanto, vamos focar na função de favoritar.
    fun toggleFavoriteStatus(evento: Evento)
}