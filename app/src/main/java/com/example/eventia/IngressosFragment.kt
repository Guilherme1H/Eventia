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
import com.example.eventia.databinding.FragmentIngressosBinding
import com.example.eventia.utils.DateUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IngressosFragment : Fragment() {

    private var _binding: FragmentIngressosBinding? = null
    private val binding get() = _binding!!

    private lateinit var ingressoAdapter: EventoAdapter
    private lateinit var ingressoManager: IngressoManager
    private val apiService by lazy { RetrofitClient.instance.create(ApiService::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngressosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = SessionManager.getUserId(requireContext())
        ingressoManager = IngressoManager(requireContext(), userId)

        setupRecyclerView()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        loadMyTickets()
    }

    private fun setupRecyclerView() {
        ingressoAdapter = EventoAdapter(mutableListOf(), null) { evento ->
            val ingressoComprado = ingressoManager.getIngressosComprados()
                .firstOrNull { it.eventoId == evento.id }

            if (ingressoComprado != null) {
                val intent = Intent(context, VisualizacaoIngressoActivity::class.java)
                intent.putExtra("EXTRA_INGRESSO", ingressoComprado) // Envia o objeto IngressoComprado
                startActivity(intent)
            } else {
                Toast.makeText(context, "Detalhes do ingresso não encontrados.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.recyclerViewMeusIngressos.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewMeusIngressos.adapter = ingressoAdapter
    }

    private fun setupClickListeners() {
        binding.buttonExplorarEventos.setOnClickListener {
            (activity as? DashboardActivity)?.navigateToTab(0)
        }
    }

    private fun loadMyTickets() {
        setLoadingState(true)
        apiService.getEventos().enqueue(object : Callback<List<Evento>> {
            override fun onResponse(call: Call<List<Evento>>, response: Response<List<Evento>>) {
                setLoadingState(false)
                if (response.isSuccessful) {
                    val todosOsEventos = response.body() ?: emptyList()
                    val idsIngressosComprados = ingressoManager.getIngressosCompradosIds()

                    val eventosComprados = todosOsEventos.filter { idsIngressosComprados.contains(it.id.toString()) }

                    val ingressosValidos = eventosComprados.filter { evento ->
                        DateUtils.isEventUpcoming(evento.data)
                    }

                    if (ingressosValidos.isEmpty()) {
                        binding.recyclerViewMeusIngressos.isVisible = false
                        binding.layoutSemIngressos.isVisible = true
                        binding.textoLayoutSemIngressos.text = "Você não possui ingressos para eventos futuros."
                    } else {
                        binding.recyclerViewMeusIngressos.isVisible = true
                        binding.layoutSemIngressos.isVisible = false
                        ingressoAdapter.atualizarLista(ingressosValidos)
                    }
                } else {
                    handleApiError()
                }
            }
            override fun onFailure(call: Call<List<Evento>>, t: Throwable) {
                setLoadingState(false)
                handleApiError()
            }
        })
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        if (isLoading) {
            binding.recyclerViewMeusIngressos.isVisible = false
            binding.layoutSemIngressos.isVisible = false
        }
    }

    private fun handleApiError() {
        binding.recyclerViewMeusIngressos.isVisible = false
        binding.layoutSemIngressos.isVisible = true
        binding.textoLayoutSemIngressos.text = "Não foi possível carregar seus ingressos."
        Toast.makeText(context, "Erro ao carregar seus ingressos.", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}