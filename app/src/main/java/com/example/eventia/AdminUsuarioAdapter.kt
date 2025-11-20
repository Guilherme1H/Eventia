package com.example.eventia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventia.databinding.ItemAdminUsuarioBinding

class AdminUsuarioAdapter(
    private var usuarios: List<LoginResponse>,
    private val onEditClick: (LoginResponse) -> Unit, // Listener para Editar
    private val onDeleteClick: (LoginResponse) -> Unit // Listener para Excluir
) : RecyclerView.Adapter<AdminUsuarioAdapter.AdminUsuarioViewHolder>() {

    inner class AdminUsuarioViewHolder(private val binding: ItemAdminUsuarioBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(usuario: LoginResponse) {
            binding.textAdminNomeUsuario.text = usuario.name
            binding.textAdminEmailUsuario.text = usuario.email

            // Configura o clique no botão EDITAR
            binding.buttonAdminEditarUsuario.setOnClickListener {
                onEditClick(usuario)
            }

            // Configura o clique no botão EXCLUIR
            binding.buttonAdminExcluirUsuario.setOnClickListener {
                onDeleteClick(usuario)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminUsuarioViewHolder {
        val binding = ItemAdminUsuarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminUsuarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminUsuarioViewHolder, position: Int) {
        holder.bind(usuarios[position])
    }

    override fun getItemCount(): Int = usuarios.size

    fun updateData(newUsuarios: List<LoginResponse>) {
        this.usuarios = newUsuarios
        notifyDataSetChanged()
    }
}