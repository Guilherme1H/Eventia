package com.example.eventia

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventia.databinding.ActivityFormularioUsuarioBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormularioUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioUsuarioBinding
    private val apiService by lazy { RetrofitClient.instance.create(ApiService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarFormularioUsuario)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarFormularioUsuario.setNavigationOnClickListener { finish() }

        binding.btnSalvarUsuario.setOnClickListener {
            salvarUsuario()
        }
    }

    private fun salvarUsuario() {
        val name = binding.etUserName.text.toString().trim()
        val email = binding.etUserEmail.text.toString().trim()
        val password = binding.etUserPassword.text.toString().trim()
        val role = if (binding.switchIsAdmin.isChecked) "admin" else "user"

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        apiService.criarUsuario(name, email, password, role).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@FormularioUsuarioActivity, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                } else {
                    Toast.makeText(this@FormularioUsuarioActivity, "Erro ao criar usuário.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@FormularioUsuarioActivity, "Falha na conexão: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}