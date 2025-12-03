package com.example.eventia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventia.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventoAdapter: EventoAdapter
    private lateinit var favoriteManager: FavoriteManager

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteManager = FavoriteManager(requireContext(), SessionManager.getUserId(requireContext()))

        setupRecyclerView()
        setupSearchView()
        observeViewModel()
    }

    private fun setupSearchView() {
        binding.searchBar.setOnQueryTextListener(this)
        binding.searchBar.queryHint = "Pesquisar eventos, shows..."
    }

    private fun observeViewModel() {
        homeViewModel.eventos.observe(viewLifecycleOwner, Observer { eventosDaApi ->
            eventosDaApi.forEach { evento ->
                evento.isFavorito = favoriteManager.isFavorite(evento.id)
            }
            eventoAdapter.atualizarLista(eventosDaApi)
        })

        homeViewModel.erro.observe(viewLifecycleOwner, Observer { mensagemErro ->
            if (mensagemErro != null && mensagemErro.isNotEmpty()) {
                Toast.makeText(context, mensagemErro, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView() {
        eventoAdapter = EventoAdapter(mutableListOf(), favoriteManager) { evento ->
            val intent = Intent(context, DetalhesEventoActivity::class.java)
            intent.putExtra("EXTRA_EVENTO", evento)
            startActivity(intent)
        }

        binding.recyclerViewEventos.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewEventos.adapter = eventoAdapter
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        binding.searchBar.clearFocus()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText.isNullOrBlank()) {
            homeViewModel.searchEvents(newText)
        } else {
            homeViewModel.carregarEventos(null)
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}