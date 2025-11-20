package com.example.eventia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val apiService by lazy { RetrofitClient.instance.create(ApiService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (SessionManager.isLoggedIn(this)) {
            irParaHome()
            return
        }

        setContentView(R.layout.activity_main)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val esqueciSenhaTextView = findViewById<TextView>(R.id.esqueciSenhaTextView)
        val cadastroTextView = findViewById<TextView>(R.id.createAccountTextView)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha o email e a senha.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            apiService.loginUser(email, password).enqueue(object : Callback<List<LoginResponse>> {
                override fun onResponse(call: Call<List<LoginResponse>>, response: Response<List<LoginResponse>>) {
                    if (response.isSuccessful) {
                        val userList = response.body()
                        if (userList != null && userList.isNotEmpty()) {
                            val user = userList[0]

                            SessionManager.saveSession(this@MainActivity, user)

                            Toast.makeText(this@MainActivity, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()

                            irParaHome()

                        } else {
                            Toast.makeText(this@MainActivity, "Email ou senha inválidos.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Erro na resposta do servidor.", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<List<LoginResponse>>, t: Throwable) {
                    Log.e("LOGIN_API", "Falha na chamada: ${t.message}", t)
                    Toast.makeText(this@MainActivity, "Erro de conexão: Verifique o IP e se o XAMPP está rodando.", Toast.LENGTH_LONG).show()
                }
            })
        }

        esqueciSenhaTextView.setOnClickListener {
            Toast.makeText(this, "Função ainda não implementada.", Toast.LENGTH_SHORT).show()
        }

        cadastroTextView.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun irParaHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}