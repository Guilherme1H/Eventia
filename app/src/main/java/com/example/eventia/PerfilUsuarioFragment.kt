package com.example.eventia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventia.databinding.FragmentPerfilUsuarioBinding
import com.example.eventia.utils.DateUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilUsuarioFragment : Fragment() {

    private var _binding: FragmentPerfilUsuarioBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoritoAdapter: EventoAdapter
    private lateinit var ingressoAdapter: EventoAdapter

    private lateinit var favoriteManager: FavoriteManager
    private lateinit var ingressoManager: IngressoManager
    private val apiService by lazy { RetrofitClient.instance.create(ApiService::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = SessionManager.getUserId(requireContext())
        favoriteManager = FavoriteManager(requireContext(), userId)
        ingressoManager = IngressoManager(requireContext(), userId)

        setupRecyclerViews()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()
        fetchDataForBothLists()
    }

    private fun setupRecyclerViews() {
        val onEventoClick: (Evento) -> Unit = { evento ->
            val intent = Intent(context, DetalhesEventoActivity::class.java)
            intent.putExtra("EXTRA_EVENTO", evento)
            startActivity(intent)
        }

        favoritoAdapter = EventoAdapter(mutableListOf(), favoriteManager, onEventoClick)
        binding.recyclerViewFavoritos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFavoritos.adapter = favoritoAdapter

        ingressoAdapter = EventoAdapter(mutableListOf(), null, onEventoClick)
        binding.recyclerViewIngressos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewIngressos.adapter = ingressoAdapter
    }

    private fun fetchDataForBothLists() {
        setLoadingState(true)
        apiService.getEventos().enqueue(object : Callback<List<Evento>> {
            override fun onResponse(call: Call<List<Evento>>, response: Response<List<Evento>>) {
                setLoadingState(false)
                if (response.isSuccessful) {
                    val todosOsEventos = response.body() ?: emptyList()

                    val eventosFavoritos = todosOsEventos.filter { favoriteManager.isFavorite(it.id) }
                    binding.layoutSemFavoritos.isVisible = eventosFavoritos.isEmpty()
                    binding.recyclerViewFavoritos.isVisible = eventosFavoritos.isNotEmpty()
                    favoritoAdapter.atualizarLista(eventosFavoritos)

                    val idsIngressosComprados = ingressoManager.getIngressosCompradosIds()
                    val eventosComprados = todosOsEventos.filter { idsIngressosComprados.contains(it.id.toString()) }

                    val ingressosValidosPreview = eventosComprados.filter { DateUtils.isEventUpcoming(it.data) }

                    binding.layoutSemIngressos.isVisible = ingressosValidosPreview.isEmpty()
                    binding.recyclerViewIngressos.isVisible = ingressosValidosPreview.isNotEmpty()
                    ingressoAdapter.atualizarLista(ingressosValidosPreview)

                } else {
                    handleApiError()
                }
            }

            override fun onFailure(call: Call<List<Evento>>, t: Throwable) {
                setLoadingState(false)
                handleApiError()
                if (context != null) {
                    Toast.makeText(context, "Falha de conexão: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupClickListeners() {

        binding.optionHistorico.setOnClickListener {
            startActivity(Intent(requireContext(), HistoricoActivity::class.java))
        }

        binding.optionLogout.setOnClickListener { fazerLogout() }

        binding.buttonGerenciarEventos.setOnClickListener {
            startActivity(Intent(requireContext(), AdminEventosActivity::class.java))
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.progressBarFavoritos.isVisible = isLoading
        binding.progressBarIngressos.isVisible = isLoading
        if (isLoading) {
            binding.recyclerViewFavoritos.isVisible = false
            binding.recyclerViewIngressos.isVisible = false
            binding.layoutSemFavoritos.isVisible = false
            binding.layoutSemIngressos.isVisible = false
        }
    }

    private fun handleApiError() {
        binding.layoutSemFavoritos.isVisible = true
        binding.recyclerViewFavoritos.isVisible = false
        binding.layoutSemIngressos.isVisible = true
        binding.recyclerViewIngressos.isVisible = false
    }

    private fun loadUserProfile() {
        binding.textViewNomeUsuarioPerfil.text = SessionManager.getUserName(requireContext())
        binding.textViewEmailUsuarioPerfil.text = SessionManager.getEmail(requireContext())
        binding.buttonGerenciarEventos.isVisible = (SessionManager.getRole(requireContext()) == "admin")
    }

    private fun fazerLogout() {
        SessionManager.logout(requireContext())
        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}