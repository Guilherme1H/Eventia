package com.example.eventia

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.eventia.databinding.ActivityEsqueciSenhaBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EsqueciSenhaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEsqueciSenhaBinding
    private val apiService by lazy { RetrofitClient.instance.create(ApiService::class.java) }
    private lateinit var progressDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEsqueciSenhaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupProgressDialog()
        setupListeners()
    }

    private fun setupToolbar() {
        // Usa a Toolbar definida no XML para navegação
        binding.toolbarReset.setNavigationOnClickListener { finish() }
    }

    private fun setupListeners() {
        binding.buttonSolicitarReset.setOnClickListener {
            val email = binding.editTextEmailReset.text.toString().trim()
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Por favor, insira um e-mail válido.", Toast.LENGTH_SHORT).show()
            } else {
                requestPasswordReset(email)
            }
        }
    }

    private fun requestPasswordReset(email: String) {
        showProgressDialog()
        binding.buttonSolicitarReset.isEnabled = false

        val request = ResetPasswordRequest(email)
        apiService.requestPasswordReset(request).enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                hideProgressDialog()
                binding.buttonSolicitarReset.isEnabled = true

                // O servidor PHP retorna 200 OK e a mensagem de sucesso por segurança,
                // independentemente de o email existir ou não.
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@EsqueciSenhaActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    // Trata possíveis erros do servidor (e.g., erro 500 ou JSON inválido)
                    Toast.makeText(this@EsqueciSenhaActivity, "Erro ao processar a solicitação. Tente novamente.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                hideProgressDialog()
                binding.buttonSolicitarReset.isEnabled = true
                Toast.makeText(this@EsqueciSenhaActivity, "Falha na conexão: Verifique sua internet.", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Funções auxiliares para o diálogo de progresso

    private fun setupProgressDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        builder.setView(inflater.inflate(R.layout.dialog_progress, null))
        builder.setCancelable(false)
        progressDialog = builder.create()
    }

    private fun showProgressDialog() {
        if (::progressDialog.isInitialized && !progressDialog.isShowing) {
            progressDialog.show()
        }
    }

    private fun hideProgressDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}