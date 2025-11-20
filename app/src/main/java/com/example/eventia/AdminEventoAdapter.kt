package com.example.eventia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventia.databinding.ItemAdminEventoBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AdminEventoAdapter(
    private var eventos: List<Evento>,
    private val onEditClick: (Evento) -> Unit,
    private val onDeleteClick: (Evento) -> Unit
) : RecyclerView.Adapter<AdminEventoAdapter.AdminEventoViewHolder>() {

    inner class AdminEventoViewHolder(private val binding: ItemAdminEventoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(evento: Evento) {
            binding.textAdminTituloEvento.text = evento.nome
            binding.textAdminDataEvento.text = formatarData(evento.data)

            binding.buttonAdminEditar.setOnClickListener {
                onEditClick(evento)
            }

            binding.buttonAdminExcluir.setOnClickListener {
                onDeleteClick(evento)
            }
        }

        private fun formatarData(dataString: String): String {
            return try {
                val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dataFormatada = parser.parse(dataString)
                if (dataFormatada != null) formatter.format(dataFormatada) else "Data inválida"
            } catch (e: Exception) {
                "Data inválida"
            }
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

    override fun onBindViewHolder(holder: AdminEventoViewHolder, position: Int) {
        holder.bind(eventos[position])
    }

    override fun getItemCount(): Int = eventos.size

    fun updateData(newEventos: List<Evento>) {
        this.eventos = newEventos
        notifyDataSetChanged()
    }
}