package com.example.eventia

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class EditarPerfilActivity : AppCompatActivity() {

    private lateinit var nomeEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var cpfEditText: EditText
    private lateinit var telefoneEditText: EditText
    private lateinit var dataNascimentoEditText: EditText
    private lateinit var senhaEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        val toolbar: Toolbar = findViewById(R.id.toolbar_editar_perfil)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Editar Perfil"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        nomeEditText = findViewById(R.id.edit_text_nome_completo)
        emailEditText = findViewById(R.id.edit_text_email)
        cpfEditText = findViewById(R.id.edit_text_cpf)
        telefoneEditText = findViewById(R.id.edit_text_telefone)
        dataNascimentoEditText = findViewById(R.id.edit_text_data_nascimento)
        senhaEditText = findViewById(R.id.edit_text_senha)

        carregarDadosDoUsuario()

        val salvarButton: Button = findViewById(R.id.button_salvar_perfil)
        salvarButton.setOnClickListener {
            salvarAlteracoes()
        }
    }

    private fun carregarDadosDoUsuario() {
        val sharedPreferences = getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)

        nomeEditText.setText(sharedPreferences.getString("USER_FULL_NAME", ""))
        emailEditText.setText(sharedPreferences.getString("USER_EMAIL", ""))
        cpfEditText.setText(sharedPreferences.getString("USER_CPF", ""))
        telefoneEditText.setText(sharedPreferences.getString("USER_PHONE", ""))
        dataNascimentoEditText.setText(sharedPreferences.getString("USER_BIRTHDATE", ""))

        senhaEditText.hint = "Digite uma nova senha (opcional)"
    }

    private fun salvarAlteracoes() {
        val sharedPreferences = getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("USER_FULL_NAME", nomeEditText.text.toString())
        editor.putString("USER_EMAIL", emailEditText.text.toString())
        editor.putString("USER_CPF", cpfEditText.text.toString())
        editor.putString("USER_PHONE", telefoneEditText.text.toString())
        editor.putString("USER_BIRTHDATE", dataNascimentoEditText.text.toString())

        if (senhaEditText.text.isNotBlank()) {
            editor.putString("USER_PASSWORD", senhaEditText.text.toString())
        }

        editor.apply()

        Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
        finish()
    }
}