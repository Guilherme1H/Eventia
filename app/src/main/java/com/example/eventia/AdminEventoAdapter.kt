package com.example.eventia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventia.databinding.ItemAdminEventoBinding

class AdminEventoAdapter(
    private var eventos: List<Evento>,
    private val onEditClick: (Evento) -> Unit,
    private val onDeleteClick: (Evento) -> Unit
) : RecyclerView.Adapter<AdminEventoAdapter.AdminEventoViewHolder>() {

    inner class AdminEventoViewHolder(val binding: ItemAdminEventoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(evento: Evento) {
            binding.textAdminTituloEvento.text = evento.nome
            binding.textAdminDataEvento.text = evento.data // TODO: Formatar data se necessário

            binding.buttonAdminEditar.setOnClickListener { onEditClick(evento) }
            binding.buttonAdminExcluir.setOnClickListener { onDeleteClick(evento) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminEventoViewHolder {
        val binding = ItemAdminEventoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AdminEventoViewHolder(binding)
    }

    override fun getItemCount() = eventos.size

    override fun onBindViewHolder(holder: AdminEventoViewHolder, position: Int) {
        holder.bind(eventos[position])
    }

    fun updateData(newEventos: List<Evento>) {
        eventos = newEventos
        notifyDataSetChanged()
    }
}