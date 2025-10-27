package com.example.eventia

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventia.Evento
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventoAdapter: EventoAdapter
    private val listaEventos = mutableListOf<Evento>()

    private val db by lazy { FirebaseFirestore.getInstance() }

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

        buscarEventosDoFirebase()
    }

    private fun buscarEventosDoFirebase() {
        db.collection("eventos")
            .orderBy("data", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                listaEventos.clear()
                for (document in result) {
                    val evento = document.toObject(Evento::class.java).copy(id = document.id)
                    listaEventos.add(evento)
                    Log.d("HomeFragment", "Evento carregado: ${evento.nome}")
                }
                eventoAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("HomeFragment", "Erro ao buscar eventos.", exception)
            }
    }
}