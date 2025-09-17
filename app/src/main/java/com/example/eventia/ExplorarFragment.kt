package com.example.eventia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ExplorarFragment : Fragment() {

    private lateinit var categoriasRecyclerView: RecyclerView
    private lateinit var categoriaAdapter: CategoriaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_explorar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriasRecyclerView = view.findViewById(R.id.recycler_view_categorias)

        categoriasRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val listaDeCategorias = listOf(
            Categoria("Festas", R.drawable.ic_celebration),
            Categoria("Shows", R.drawable.ic_mic),
            Categoria("Teatro", R.drawable.ic_theater_masks),
            Categoria("Esportes", R.drawable.ic_sports_soccer),
            Categoria("Palestras", R.drawable.ic_campaign)
        )

        categoriaAdapter = CategoriaAdapter(listaDeCategorias)
        categoriasRecyclerView.adapter = categoriaAdapter
    }
}