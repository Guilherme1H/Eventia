package com.example.eventia

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventia.databinding.ActivityAdminEventosBinding
import com.example.eventia.FormularioEventoActivity

class AdminEventosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminEventosBinding
    private lateinit var adminEventoAdapter: AdminEventoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminEventosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarAdminEventos)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAdminEventos.setNavigationOnClickListener {
            finish()
        }

        setupRecyclerView()
        setupClickListeners()
        loadEventos()
    }

    private fun setupRecyclerView() {
        adminEventoAdapter = AdminEventoAdapter(
            eventos = emptyList(),
            onEditClick = { evento ->
                // TODO: Navegar para a tela de edição
                Toast.makeText(this, "Editar: ${evento.nome}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { evento ->
                // TODO: Implementar a lógica de exclusão com confirmação
                Toast.makeText(this, "Excluir: ${evento.nome}", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recyclerViewAdminEventos.apply {
            layoutManager = LinearLayoutManager(this@AdminEventosActivity)
            adapter = adminEventoAdapter
        }
    }

    private fun setupClickListeners() {
        binding.fabAdicionarEvento.setOnClickListener {
            val intent = Intent(this, FormularioEventoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadEventos() {
        val listaSimulada = listOf(
            Evento(id = "1", nome = "Show de Rock", data = "2025-12-10T20:00", local = "Estádio Morumbi", preco = 150.0, imagemUrl = ""),
            Evento(id = "2", nome = "Festival de Jazz", data = "2025-11-22T18:00", local = "Parque Ibirapuera", preco = 0.0, imagemUrl = ""),
            Evento(id = "3", nome = "Peça de Teatro", data = "2025-12-05T21:00", local = "Teatro Municipal", preco = 80.0, imagemUrl = "")
        )

        adminEventoAdapter.updateData(listaSimulada)
    }
}