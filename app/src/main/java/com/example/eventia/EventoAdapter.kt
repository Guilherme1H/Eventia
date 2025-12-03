package com.example.eventia

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventia.databinding.ItemEventoVerticalBinding
import com.example.eventia.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

class EventoAdapter(
    private var eventos: MutableList<Evento>,
    private val favoriteManager: FavoriteManager?,
    private val onItemClicked: (Evento) -> Unit
) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    inner class EventoViewHolder(val binding: ItemEventoVerticalBinding) : RecyclerView.ViewHolder(binding.root) {

        private val context: Context = binding.root.context

        fun bind(evento: Evento) {
            itemView.setOnClickListener {
                onItemClicked(evento)
            }

            binding.textTituloEvento.text = evento.nome
            binding.textLocalEvento.text = evento.local
            binding.textDataEvento.text = formatarData(evento.data)

            if (evento.preco > 0.0) {
                binding.textPrecoEvento.text = "R\$ ${String.format(Locale("pt", "BR"), "%.2f", evento.preco)}"
            } else {
                binding.textPrecoEvento.text = "Grátis"
            }

            val imageUrl = evento.imagemUrl ?: ""

            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(binding.imageEvento)

            if (!DateUtils.isEventUpcoming(evento.data)) {
                itemView.alpha = 0.6f
            } else {
                itemView.alpha = 1.0f
            }

            if (favoriteManager != null) {
                binding.iconFavorito.isVisible = true
                atualizarIconeFavorito(evento.isFavorito)

                binding.iconFavorito.setOnClickListener {
                    evento.isFavorito = !evento.isFavorito
                    atualizarIconeFavorito(evento.isFavorito)

                    if (evento.isFavorito) {
                        favoriteManager.addFavorite(evento.id)
                    } else {
                        favoriteManager.removeFavorite(evento.id)
                    }
                }
            } else {
                binding.iconFavorito.isVisible = false
            }
        }

        private fun atualizarIconeFavorito(isFavorito: Boolean) {
            val colorRes = if (isFavorito) R.color.accent_brand else R.color.white
            binding.iconFavorito.setColorFilter(ContextCompat.getColor(context, colorRes))
        }

        private fun formatarData(dataIso: String): String {
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formatter = SimpleDateFormat("dd 'de' MMMM '•' HH:mm", Locale("pt", "BR"))
            return try {
                parser.parse(dataIso)?.let { formatter.format(it) } ?: dataIso
            } catch (e: Exception) {
                dataIso
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val binding = ItemEventoVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        holder.bind(eventos[position])
    }

    override fun getItemCount() = eventos.size

    fun atualizarLista(novosEventos: List<Evento>) {
        eventos.clear()
        eventos.addAll(novosEventos)
        notifyDataSetChanged()
    }
}