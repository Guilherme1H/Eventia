package com.example.eventia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.eventia.databinding.FragmentPerfilUsuarioBinding

class PerfilUsuarioFragment : Fragment() {

    private var _binding: FragmentPerfilUsuarioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserProfile()
        setupClickListeners()
    }

    private fun loadUserProfile() {
        val sharedPreferences = requireActivity().getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE)
        val nomeUsuario = sharedPreferences.getString("USER_FULL_NAME", "Usuário")
        val emailUsuario = sharedPreferences.getString("USER_EMAIL", "seu@email.com")

        binding.textViewNomeUsuarioPerfil.text = nomeUsuario
        binding.textViewEmailUsuarioPerfil.text = emailUsuario

        val isAdmin = true

        if (isAdmin) {
            binding.buttonGerenciarEventos.visibility = View.VISIBLE
        } else {
            binding.buttonGerenciarEventos.visibility = View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.buttonConfiguracoes.setOnClickListener {
            val intent = Intent(activity, ConfiguracoesActivity::class.java)
            startActivity(intent)
        }

        binding.optionHistorico.setOnClickListener {
            Toast.makeText(context, "Tela de Histórico em desenvolvimento.", Toast.LENGTH_SHORT).show()
        }

        binding.optionAjuda.setOnClickListener {
            Toast.makeText(context, "Central de Ajuda em breve!", Toast.LENGTH_SHORT).show()
        }

        binding.optionLogout.setOnClickListener {
            fazerLogout()
        }

        binding.buttonGerenciarEventos.setOnClickListener {
            val intent = Intent(requireContext(), AdminEventosActivity::class.java)
            startActivity(intent)
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}