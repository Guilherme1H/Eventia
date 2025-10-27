package com.example.eventia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventia.databinding.FragmentExplorarBinding

class ExplorarFragment : Fragment() {

    private var _binding: FragmentExplorarBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoriaAdapter: CategoriaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExplorarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCategorias.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val listaDeCategorias = listOf(
            Categoria("Festas", R.drawable.ic_celebration),
            Categoria("Shows", R.drawable.ic_mic),
            Categoria("Teatro", R.drawable.ic_theater_masks),
            Categoria("Esportes", R.drawable.ic_sports_soccer),
            Categoria("Palestras", R.drawable.ic_campaign)
        )

        categoriaAdapter = CategoriaAdapter(listaDeCategorias)
        binding.recyclerViewCategorias.adapter = categoriaAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}