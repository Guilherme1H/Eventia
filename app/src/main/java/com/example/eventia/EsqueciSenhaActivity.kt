package com.example.eventia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat

class EsqueciSenhaActivity : AppCompatActivity() {

    private lateinit var inputGroup: LinearLayout
    private lateinit var emailRecuperacaoEditText: EditText
    private lateinit var enviarButton: Button
    private lateinit var backButton: ImageButton

    private lateinit var successGroup: LinearLayout
    private lateinit var successMessageTextView: TextView
    private lateinit var voltarLoginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_esqueci_senha)

        backButton = findViewById(R.id.backButton)
        inputGroup = findViewById(R.id.inputGroup)
        emailRecuperacaoEditText = findViewById(R.id.emailRecuperacaoEditText)
        enviarButton = findViewById(R.id.enviarButton)

        successGroup = findViewById(R.id.successGroup)
        successMessageTextView = findViewById(R.id.successMessageTextView)
        voltarLoginButton = findViewById(R.id.voltarLoginButton)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        backButton.setOnClickListener {
            if (successGroup.visibility == View.VISIBLE) {
                finish()
            } else {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        enviarButton.setOnClickListener {
            val email = emailRecuperacaoEditText.text.toString().trim()
            if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showSuccessScreen(email)
            } else {
                Toast.makeText(this, "Por favor, insira um e-mail válido.", Toast.LENGTH_SHORT).show()
            }
        }

        voltarLoginButton.setOnClickListener {
            finish()
        }
    }

    private fun showSuccessScreen(email: String) {
        backButton.visibility = View.GONE

        inputGroup.visibility = View.GONE

        successMessageTextView.text = "Enviamos as instruções de recuperação para $email. Não se esqueça de checar a caixa de spam."
        successGroup.visibility = View.VISIBLE
    }
}