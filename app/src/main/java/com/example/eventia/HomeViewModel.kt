package com.example.eventia

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventia.utils.DateUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val apiService by lazy { RetrofitClient.instance.create(ApiService::class.java) }

    private val _eventos = MutableLiveData<List<Evento>>()
    val eventos: LiveData<List<Evento>> get() = _eventos

    private val _erro = MutableLiveData<String>()
    val erro: LiveData<String> get() = _erro

    init {
        carregarEventos(null)
    }

    fun carregarEventos(categoria: String?) {
        val call: Call<List<Evento>> = if (categoria.isNullOrEmpty() || categoria == "Todos") {
            apiService.getEventos()
        } else {
            apiService.getEventosPorCategoria(categoria)
        }

        executeCall(call, "Filtro: $categoria")
    }

    fun searchEvents(query: String) {
        if (query.isBlank()) {
            carregarEventos(null)
            return
        }

        val call = apiService.searchEvents(query)

        executeCall(call, "Busca: $query")
    }

    private fun executeCall(call: Call<List<Evento>>, logContext: String) {
        call.enqueue(object : Callback<List<Evento>> {
            override fun onResponse(call: Call<List<Evento>>, response: Response<List<Evento>>) {
                if (response.isSuccessful) {
                    val todosOsEventos = response.body() ?: emptyList()

                    val eventosFuturos = todosOsEventos.filter { evento ->
                        DateUtils.isEventUpcoming(evento.data)
                    }

                    _eventos.value = eventosFuturos
                    Log.d("HomeViewModel", "${eventosFuturos.size} eventos carregados ($logContext).")

                } else {
                    _erro.value = "Falha ao carregar eventos. Código: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<List<Evento>>, t: Throwable) {
                _erro.value = "Erro de conexão: ${t.message}"
            }
        })
    }
}