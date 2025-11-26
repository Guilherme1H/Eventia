package com.example.eventia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventia.databinding.ActivityHistoricoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoricoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricoBinding
    private lateinit var historicoAdapter: EventoAdapter
    private lateinit var ingressoManager: IngressoManager
    private val apiService by lazy { RetrofitClient.instance.create(ApiService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = SessionManager.getUserId(this)
        ingressoManager = IngressoManager(this, userId)

        binding.toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        loadPurchaseHistory()
    }

    private fun setupRecyclerView() {
        historicoAdapter = EventoAdapter(mutableListOf(), null) {}
        binding.recyclerViewHistorico.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewHistorico.adapter = historicoAdapter
    }

    private fun loadPurchaseHistory() {
        apiService.getEventos().enqueue(object : Callback<List<Evento>> {
            override fun onResponse(call: Call<List<Evento>>, response: Response<List<Evento>>) {
                if (response.isSuccessful) {
                    val todosOsEventos = response.body() ?: emptyList()
                    val idsIngressosComprados = ingressoManager.getIngressosCompradosIds()

                    val historicoDeEventos = todosOsEventos.filter { idsIngressosComprados.contains(it.id.toString()) }

                    historicoAdapter.atualizarLista(historicoDeEventos)
                }
            }
            override fun onFailure(call: Call<List<Evento>>, t: Throwable) {}
        })
    }
}