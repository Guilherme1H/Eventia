package com.example.eventia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

class ConfiguracoesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracoes)

        val toolbar: Toolbar = findViewById(R.id.toolbar_configuracoes)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val editarPerfil: TextView = findViewById(R.id.option_editar_perfil)
        val notificacoes: TextView = findViewById(R.id.option_notificacoes)
        val privacidade: TextView = findViewById(R.id.option_privacidade)
        val logout: TextView = findViewById(R.id.option_logout_config)

        editarPerfil.setOnClickListener {
            val intent = Intent(this, EditarPerfilActivity::class.java)
            startActivity(intent)
        }

        notificacoes.setOnClickListener {
            Toast.makeText(this, "Funcionalidade em desenvolvimento.", Toast.LENGTH_SHORT).show()
        }

        privacidade.setOnClickListener {
            Toast.makeText(this, "Funcionalidade em desenvolvimento.", Toast.LENGTH_SHORT).show()
        }

        logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}