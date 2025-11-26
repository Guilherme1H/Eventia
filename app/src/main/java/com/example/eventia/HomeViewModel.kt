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
        carregarEventos()
    }

    private fun carregarEventos() {
        apiService.getEventos().enqueue(object : Callback<List<Evento>> {
            override fun onResponse(call: Call<List<Evento>>, response: Response<List<Evento>>) {
                if (response.isSuccessful) {
                    val todosOsEventos = response.body() ?: emptyList()

                    val eventosFuturos = todosOsEventos.filter { evento ->
                        DateUtils.isEventUpcoming(evento.data)
                    }

                    _eventos.value = eventosFuturos
                    Log.d("HomeViewModel", "${eventosFuturos.size} eventos futuros carregados.")

                } else {
                    _erro.value = "Falha ao carregar eventos."
                }
            }

            override fun onFailure(call: Call<List<Evento>>, t: Throwable) {
                _erro.value = "Erro de conexão: ${t.message}"
            }
        })
    }
}