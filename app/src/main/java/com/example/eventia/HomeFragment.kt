package com.example.eventia

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventia.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

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
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteManager = FavoriteManager(requireContext(), SessionManager.getUserId(requireContext()))

        setupRecyclerView()
        observeViewModel()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                startActivity(Intent(requireContext(), CarrinhoActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}