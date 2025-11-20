package com.example.eventia

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
        val nomeUsuario = SessionManager.getUserName(requireContext())

        binding.textViewNomeUsuarioPerfil.text = nomeUsuario
        // binding.textViewEmailUsuarioPerfil.text = emailUsuario

        val userRole = SessionManager.getRole(requireContext())

        if (userRole == "admin") {
            binding.buttonGerenciarEventos.visibility = View.VISIBLE
        } else {
            binding.buttonGerenciarEventos.visibility = View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.buttonConfiguracoes.setOnClickListener {
            Toast.makeText(context, "Tela de Configurações em desenvolvimento.", Toast.LENGTH_SHORT).show()
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
        SessionManager.logout(requireContext())

        val intent = Intent(requireActivity(), MainActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}