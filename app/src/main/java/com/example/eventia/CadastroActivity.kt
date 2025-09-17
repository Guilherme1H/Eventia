package com.example.eventia

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class CadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val backButton = findViewById<ImageButton>(R.id.backButtonCadastro)
        val nomeEditText = findViewById<EditText>(R.id.nomeEditTextCadastro)
        val emailEditText = findViewById<EditText>(R.id.emailEditTextCadastro)
        val senhaEditText = findViewById<EditText>(R.id.senhaEditTextCadastro)
        val cpfEditText = findViewById<EditText>(R.id.cpfEditTextCadastro)
        val telefoneEditText = findViewById<EditText>(R.id.telefoneEditTextCadastro)
        val dataNascimentoEditText = findViewById<EditText>(R.id.dataNascimentoEditTextCadastro)
        val passwordStrengthTextView = findViewById<TextView>(R.id.passwordStrengthTextView)
        val termsCheckBox = findViewById<CheckBox>(R.id.termsCheckBox)
        val createAccountButton = findViewById<Button>(R.id.createAccountButtonCadastro)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        senhaEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrBlank()) {
                    passwordStrengthTextView.visibility = View.GONE
                } else {
                    passwordStrengthTextView.visibility = View.VISIBLE
                    if (s.length < 8) {
                        passwordStrengthTextView.text = "A senha deve ter no mínimo 8 caracteres."
                        passwordStrengthTextView.setTextColor(Color.RED)
                    } else {
                        passwordStrengthTextView.text = "Força da senha: Forte"
                        passwordStrengthTextView.setTextColor(Color.GREEN)
                    }
                }
            }
        })

        createAccountButton.setOnClickListener {
            if (!termsCheckBox.isChecked) {
                Toast.makeText(this, "Você deve aceitar os Termos de Uso.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nome = nomeEditText.text.toString()
            val email = emailEditText.text.toString()
            val senha = senhaEditText.text.toString()
            val cpf = cpfEditText.text.toString()
            val telefone = telefoneEditText.text.toString()
            val dataNascimento = dataNascimentoEditText.text.toString()

            if (nome.isBlank() || email.isBlank() || senha.length < 8) {
                Toast.makeText(this, "Preencha os campos obrigatórios e use uma senha com no mínimo 8 caracteres.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val sharedPreferences = getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("USER_FULL_NAME", nome)
            editor.putString("USER_EMAIL", email)
            editor.putString("USER_PASSWORD", senha)
            editor.putString("USER_CPF", cpf)
            editor.putString("USER_PHONE", telefone)
            editor.putString("USER_BIRTHDATE", dataNascimento)
            editor.apply()

            Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("USER_NAME", nome)
            intent.putExtra("USER_EMAIL", email)
            startActivity(intent)
            finishAffinity()
        }
    }
}