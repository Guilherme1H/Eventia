package com.example.eventia

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventia.databinding.ActivityEditarUsuarioBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditarUsuarioBinding
    private val apiService by lazy { RetrofitClient.instance.create(ApiService::class.java) }
    private var userId: Int = -1

    // VARIÁVEL PARA CONTROLAR O ESTADO DO CARGO (ADMIN/USER)
    private var isAdmin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarEditarUsuario.setNavigationOnClickListener {
            finish()
        }

        userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            Toast.makeText(this, "Erro: ID do usuário não encontrado.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Preenche os campos
        val userName = intent.getStringExtra("USER_NAME")
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val userRole = intent.getStringExtra("USER_ROLE")

        binding.editTextName.setText(userName)
        binding.editTextEmail.setText(userEmail)

        // *** MUDANÇA 1: Define o estado inicial da nossa variável ***
        isAdmin = (userRole == "admin")
        updateRoleButton() // Atualiza a aparência do botão

        // *** MUDANÇA 2: Lógica do clique do novo botão ***
        binding.buttonRole.setOnClickListener {
            // Inverte o estado atual (se era admin, vira user, e vice-versa)
            isAdmin = !isAdmin
            // Atualiza a aparência do botão para refletir o novo estado
            updateRoleButton()
        }

        binding.buttonSave.setOnClickListener {
            saveChanges()
        }
    }

    // *** MUDANÇA 3: Função para atualizar o texto e a cor do botão ***
    private fun updateRoleButton() {
        if (isAdmin) {
            binding.buttonRole.text = "Cargo: Administrador"
            // Você pode até mudar a cor para ficar mais claro
            binding.buttonRole.setBackgroundColor(Color.parseColor("#4CAF50")) // Um verde
        } else {
            binding.buttonRole.text = "Cargo: Usuário Comum"
            binding.buttonRole.setBackgroundColor(Color.parseColor("#757575")) // Um cinza
        }
    }

    private fun saveChanges() {
        val name = binding.editTextName.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString()

        // *** MUDANÇA 4: Lê o cargo da nossa variável, não mais do Switch ***
        val role = if (isAdmin) "admin" else "user"

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Nome e E-mail não podem ser vazios.", Toast.LENGTH_SHORT).show()
            return
        }

        val finalPassword = if (password.isNotEmpty()) password else null
        val request = UpdateUserRequest(id = userId, name = name, email = email, password = finalPassword, role = role)

        apiService.updateUser(request).enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@EditarUsuarioActivity, "Usuário atualizado com sucesso!", Toast.LENGTH_LONG).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    val errorMessage = response.body()?.message ?: "Erro ao atualizar."
                    Toast.makeText(this@EditarUsuarioActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                Toast.makeText(this@EditarUsuarioActivity, "Falha na conexão: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}