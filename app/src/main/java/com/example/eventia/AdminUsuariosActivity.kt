package com.example.eventia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventia.databinding.ActivityAdminUsuariosBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminUsuariosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminUsuariosBinding
    private lateinit var adminUsuarioAdapter: AdminUsuarioAdapter
    private val apiService by lazy { RetrofitClient.instance.create(ApiService::class.java) }

    private val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Atualizando lista...", Toast.LENGTH_SHORT).show()
            loadUsuarios()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarAdminUsuarios)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAdminUsuarios.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        loadUsuarios()

        binding.fabAdicionarUsuarios.setOnClickListener {
            val intent = Intent(this, FormularioUsuarioActivity::class.java)
            formLauncher.launch(intent)
        }
    }

    private fun setupRecyclerView() {
        adminUsuarioAdapter = AdminUsuarioAdapter(
            usuarios = emptyList(),
            onEditClick = { usuario ->
                val intent = Intent(this, EditarUsuarioActivity::class.java)

                intent.putExtra("USER_ID", usuario.id)
                intent.putExtra("USER_NAME", usuario.name)
                intent.putExtra("USER_EMAIL", usuario.email)
                intent.putExtra("USER_ROLE", usuario.role)

                formLauncher.launch(intent)
            },

            onDeleteClick = { usuario ->
                mostrarDialogoConfirmacao(usuario)
            }
        )
        binding.recyclerViewAdminUsuarios.apply {
            layoutManager = LinearLayoutManager(this@AdminUsuariosActivity)
            adapter = adminUsuarioAdapter
        }
    }

    private fun loadUsuarios() {
        apiService.getUsuarios().enqueue(object : Callback<List<LoginResponse>> {
            override fun onResponse(call: Call<List<LoginResponse>>, response: Response<List<LoginResponse>>) {
                if (response.isSuccessful) {
                    adminUsuarioAdapter.updateData(response.body() ?: emptyList())
                } else {
                    Toast.makeText(this@AdminUsuariosActivity, "Erro ao carregar usuários.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<LoginResponse>>, t: Throwable) {
                Toast.makeText(this@AdminUsuariosActivity, "Falha de conexão: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun mostrarDialogoConfirmacao(usuario: LoginResponse) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza que deseja excluir o usuário '${usuario.name}'?")
            .setPositiveButton("Sim") { _, _ -> deletarUsuario(usuario) }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun deletarUsuario(usuario: LoginResponse) {
        apiService.excluirUsuario(usuario.id).enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@AdminUsuariosActivity, "Usuário excluído!", Toast.LENGTH_SHORT).show()
                    loadUsuarios()
                } else {
                    Toast.makeText(this@AdminUsuariosActivity, response.body()?.message ?: "Erro ao excluir.", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                Toast.makeText(this@AdminUsuariosActivity, "Falha de conexão: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}