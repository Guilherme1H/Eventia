package com.example.eventia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class PerfilUsuarioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil_usuario, container, false)

        val nomeUsuarioTextView = view.findViewById<TextView>(R.id.text_view_nome_usuario_perfil)
        val emailUsuarioTextView = view.findViewById<TextView>(R.id.text_view_email_usuario_perfil)
        val settingsButton = view.findViewById<ImageButton>(R.id.button_configuracoes)

        val historicoOption = view.findViewById<TextView>(R.id.option_historico)
        val ajudaOption = view.findViewById<TextView>(R.id.option_ajuda)
        val logoutOption = view.findViewById<TextView>(R.id.option_logout)

        val sharedPreferences = requireActivity().getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)
        val nomeUsuario = sharedPreferences.getString("USER_FULL_NAME", "Usuário")
        val emailUsuario = sharedPreferences.getString("USER_EMAIL", "seu@email.com")

        nomeUsuarioTextView.text = nomeUsuario
        emailUsuarioTextView.text = emailUsuario

        settingsButton.setOnClickListener {
            val intent = Intent(activity, ConfiguracoesActivity::class.java)
            startActivity(intent)
        }

        historicoOption.setOnClickListener {
            Toast.makeText(context, "Tela de Histórico em desenvolvimento.", Toast.LENGTH_SHORT).show()
        }

        ajudaOption.setOnClickListener {
            Toast.makeText(context, "Central de Ajuda em breve!", Toast.LENGTH_SHORT).show()
        }

        logoutOption.setOnClickListener {
            fazerLogout()
        }

        return view
    }

    private fun fazerLogout() {
        val editor = requireActivity().getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()

        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }
}