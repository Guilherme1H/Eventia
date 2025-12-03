package com.example.eventia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventia.databinding.FragmentExplorarBinding
import androidx.appcompat.widget.SearchView

class ExplorarFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentExplorarBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoriaAdapter: CategoriaAdapter
    private lateinit var eventoAdapter: EventoAdapter

    private lateinit var favoriteManager: FavoriteManager
    private val homeViewModel: HomeViewModel by viewModels()

    private var categoriaSelecionada: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExplorarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteManager = FavoriteManager(requireContext(), SessionManager.getUserId(requireContext()))

        setupRecyclerViews()
        setupSearchView()
        observeViewModel()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(this)
        binding.searchView.queryHint = "Pesquisar por eventos, shows..."
    }

    private fun setupRecyclerViews() {
        eventoAdapter = EventoAdapter(mutableListOf(), favoriteManager) { evento ->
            val intent = Intent(context, DetalhesEventoActivity::class.java)
            intent.putExtra("EXTRA_EVENTO", evento)
            startActivity(intent)
        }
        binding.recyclerViewEventos.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewEventos.adapter = eventoAdapter

        val onCategoriaClick: (Categoria) -> Unit = { categoria ->
            binding.searchView.setQuery("", false)
            binding.searchView.clearFocus()

            if (categoria.nome == "Todos") {
                categoriaSelecionada = null
            } else {
                categoriaSelecionada = categoria.nome
            }
            homeViewModel.carregarEventos(categoriaSelecionada)
            Toast.makeText(requireContext(), "Filtrando por: ${categoria.nome}", Toast.LENGTH_SHORT).show()
        }

        val listaDeCategorias = listOf(
            Categoria("Todos", R.drawable.ic_home),
            Categoria("Festas", R.drawable.ic_celebration),
            Categoria("Shows", R.drawable.ic_mic),
            Categoria("Teatro", R.drawable.ic_theater_masks),
            Categoria("Esportes", R.drawable.ic_sports_soccer),
            Categoria("Palestras", R.drawable.ic_campaign)
        )

        categoriaAdapter = CategoriaAdapter(listaDeCategorias, onCategoriaClick)
        binding.recyclerViewCategorias.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewCategorias.adapter = categoriaAdapter
    }

    private fun observeViewModel() {
        homeViewModel.eventos.observe(viewLifecycleOwner, Observer { eventosDaApi ->
            eventosDaApi.forEach { evento ->
                evento.isFavorito = favoriteManager.isFavorite(evento.id)
            }
            eventoAdapter.atualizarLista(eventosDaApi)
        })

        homeViewModel.erro.observe(viewLifecycleOwner, Observer { mensagemErro ->
            Toast.makeText(context, mensagemErro, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        binding.searchView.clearFocus()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText.isNullOrBlank()) {
            homeViewModel.searchEvents(newText)
            categoriaSelecionada = null
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