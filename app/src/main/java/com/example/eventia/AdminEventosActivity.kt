package com.example.eventia

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventia.databinding.ActivityAdminEventosBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminEventosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminEventosBinding
    private lateinit var adminEventoAdapter: AdminEventoAdapter
    private val apiService by lazy { RetrofitClient.instance.create(ApiService::class.java) }
    private val formularioLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        loadEventos()
    }

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
    }

    override fun onResume() {
        super.onResume()
        loadEventos()
    }

    private fun setupRecyclerView() {
        adminEventoAdapter = AdminEventoAdapter(
            eventos = emptyList(),
            onEditClick = { evento ->
                val intent = Intent(this, FormularioEventoActivity::class.java)
                intent.putExtra("EVENTO_PARA_EDITAR", evento)
                formularioLauncher.launch(intent)
            },
            onDeleteClick = { evento ->
                mostrarDialogoConfirmacao(evento)
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
            formularioLauncher.launch(intent)
        }
    }

    private fun loadEventos() {
        apiService.getEventos().enqueue(object: Callback<List<Evento>> {
            override fun onResponse(call: Call<List<Evento>>, response: Response<List<Evento>>) {
                if (response.isSuccessful) {
                    val eventos = response.body() ?: emptyList()
                    adminEventoAdapter.updateData(eventos)
                } else {
                    Toast.makeText(this@AdminEventosActivity, "Erro ao carregar eventos.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Evento>>, t: Throwable) {
                Log.e("AdminEventosActivity", "Falha na API: ${t.message}", t)
                Toast.makeText(this@AdminEventosActivity, "Falha de conexão.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun mostrarDialogoConfirmacao(evento: Evento) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza que deseja excluir o evento '${evento.nome}'?")
            .setPositiveButton("Sim") { _, _ ->
                deletarEvento(evento)
            }
            .setNegativeButton("Não", null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun deletarEvento(evento: Evento) {
        apiService.excluirEvento(evento.id.toString()).enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@AdminEventosActivity, "Evento excluído com sucesso!", Toast.LENGTH_SHORT).show()
                    loadEventos()
                } else {
                    val errorMsg = response.body()?.message ?: "Erro ao excluir o evento."
                    Toast.makeText(this@AdminEventosActivity, errorMsg, Toast.LENGTH_LONG).show()
                    Log.e("AdminEventosActivity", "Erro ao deletar: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                Toast.makeText(this@AdminEventosActivity, "Falha de conexão ao tentar excluir.", Toast.LENGTH_SHORT).show()
                Log.e("AdminEventosActivity", "Falha na API ao deletar: ${t.message}", t)
            }
        })
    }
}