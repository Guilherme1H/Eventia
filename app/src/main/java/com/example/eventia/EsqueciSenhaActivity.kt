package com.example.eventia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EsqueciSenhaActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    private lateinit var inputGroup: LinearLayout
    private lateinit var successGroup: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esqueci_senha)

        val BASE_URL = "http://10.0.2.2/api_eventia/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val emailRecuperacaoEditText = findViewById<EditText>(R.id.emailRecuperacaoEditText)
        val enviarButton = findViewById<Button>(R.id.enviarButton)
        val voltarLoginButton = findViewById<Button>(R.id.voltarLoginButton)

        inputGroup = findViewById(R.id.inputGroup)
        successGroup = findViewById(R.id.successGroup)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        enviarButton.setOnClickListener {
            val email = emailRecuperacaoEditText.text.toString().trim()

            if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                enviarPedidoDeRecuperacao(email)
            } else {
                Toast.makeText(this, "Por favor, insira um e-mail válido.", Toast.LENGTH_SHORT).show()
            }
        }

        voltarLoginButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun enviarPedidoDeRecuperacao(email: String) {
        Toast.makeText(this, "Enviando...", Toast.LENGTH_SHORT).show()

        val request = PasswordResetRequest(email = email)

        android.os.Handler(mainLooper).postDelayed({
            showSuccessScreen(email)
        }, 2000)

        /*
        // SEU CÓDIGO REAL DE API FICARIA AQUI:
        apiService.requestPasswordReset(request).enqueue(object : Callback<Void> { // Supondo que a API não retorne corpo
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    showSuccessScreen(email)
                } else {
                    Toast.makeText(this@EsqueciSenhaActivity, "Falha ao enviar o pedido. Tente novamente.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@EsqueciSenhaActivity, "Erro de conexão: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
        */
    }

    private fun showSuccessScreen(email: String) {
        inputGroup.visibility = View.GONE

        successGroup.visibility = View.VISIBLE

        val successMessageTextView = findViewById<TextView>(R.id.successMessageTextView)
        successMessageTextView.text = "Enviamos as instruções de recuperação para $email. Não se esqueça de checar a caixa de spam."
    }
}