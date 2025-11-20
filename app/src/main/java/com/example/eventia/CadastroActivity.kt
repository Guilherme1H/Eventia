package com.example.eventia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroActivity : AppCompatActivity() {

    private val apiService by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)


        val nomeEditText = findViewById<EditText>(R.id.nomeEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditTextCadastro)
        val passwordEditText = findViewById<EditText>(R.id.senhaEditTextCadastro)
        val registerButton = findViewById<Button>(R.id.cadastrarButton)
        val backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        registerButton.setOnClickListener {
            val nome = nomeEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (nome.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val request = RegisterRequest(name = nome, email = email, password = password)

                apiService.registerUser(request).enqueue(object : Callback<RegistrationResponse> {
                    override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            successScreen(nome)
                        } else {
                            val errorMessage = response.body()?.message ?: "Ocorreu um erro no cadastro."
                            Toast.makeText(this@CadastroActivity, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                        Log.e("CADASTRO_API", "Falha na chamada: ${t.message}", t)
                        Toast.makeText(this@CadastroActivity, "Erro de conexão: Verifique o IP e se o XAMPP está rodando.", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun successScreen(nome: String) {
        Toast.makeText(this, "Cadastro de $nome realizado com sucesso!", Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}