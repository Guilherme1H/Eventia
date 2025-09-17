package com.example.eventia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class PerfilUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)

        supportActionBar?.hide()
    }
}