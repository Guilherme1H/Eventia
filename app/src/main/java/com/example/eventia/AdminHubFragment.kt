package com.example.eventia.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.eventia.AdminEventosActivity
import com.example.eventia.AdminUsuariosActivity
import com.example.eventia.SessionManager
import com.example.eventia.databinding.FragmentAdminHubBinding

class AdminHubFragment : Fragment() {

    private var _binding: FragmentAdminHubBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminHubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userRole = SessionManager.getRole(requireContext())

        val cleanedRole = userRole?.trim()?.replace("'", "")

        if (cleanedRole != "admin") {
            Toast.makeText(requireContext(), "Acesso negado. Permissões de administrador necessárias.", Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
            return
        }
        setupAdminView()
    }

    private fun setupAdminView() {
        binding.toolbarAdminHub.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.cardManageEvents.setOnClickListener {
            val intent = Intent(requireActivity(), AdminEventosActivity::class.java)
            startActivity(intent)
        }

        binding.cardManageUsers.setOnClickListener {
            val intent = Intent(requireActivity(), AdminUsuariosActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}