package com.example.eventia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val BASE_URL = "http://10.0.2.2/api_eventia/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val createAccountTextView = findViewById<TextView>(R.id.createAccountTextView)
        val esqueciSenhaTextView = findViewById<TextView>(R.id.esqueciSenhaTextView)


        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                apiService.loginUser(email, password).enqueue(object : Callback<List<LoginResponse>> {
                    override fun onResponse(call: Call<List<LoginResponse>>, response: Response<List<LoginResponse>>) {
                        if (response.isSuccessful) {
                            val userList = response.body()
                            if (!userList.isNullOrEmpty()) {
                                val user = userList[0]
                                successScreen(user.name)
                            } else {
                                Toast.makeText(this@MainActivity, "Email ou senha inválidos.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Erro de comunicação: ${response.code()}", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<List<LoginResponse>>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Erro de rede: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this@MainActivity, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        createAccountTextView.setOnClickListener {
            val intent = Intent(this@MainActivity, CadastroActivity::class.java)
            startActivity(intent)
        }

        esqueciSenhaTextView.setOnClickListener {
            val intent = Intent(this, EsqueciSenhaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun successScreen(nome: String) {
        Toast.makeText(this, "Bem-vindo(a), $nome!", Toast.LENGTH_LONG).show()
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("USER_NAME", nome)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}