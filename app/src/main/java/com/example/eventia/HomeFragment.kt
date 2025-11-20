package com.example.eventia

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventoAdapter: EventoAdapter
    private val listaEventos = mutableListOf<Evento>()

    private val apiService by lazy { RetrofitClient.instance.create(ApiService::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view_eventos)
        recyclerView.layoutManager = LinearLayoutManager(context)

        eventoAdapter = EventoAdapter(listaEventos)
        recyclerView.adapter = eventoAdapter

        buscarEventos()
    }

    private fun buscarEventos() {
        apiService.getEventos().enqueue(object: Callback<List<Evento>> {
            override fun onResponse(call: Call<List<Evento>>, response: Response<List<Evento>>) {
                if (response.isSuccessful) {
                    response.body()?.let { eventos ->
                        listaEventos.clear()
                        listaEventos.addAll(eventos)
                        eventoAdapter.notifyDataSetChanged()
                        Log.d("HomeFragment", "${eventos.size} eventos carregados.")
                    }
                } else {
                    Log.e("HomeFragment", "Erro na resposta: ${response.code()}")
                    Toast.makeText(context, "Erro ao carregar eventos.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Evento>>, t: Throwable) {
                Log.e("HomeFragment", "Falha na chamada da API.", t)
                Toast.makeText(context, "Falha de conexão.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}