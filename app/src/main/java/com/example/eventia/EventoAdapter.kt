package com.example.eventia

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class EventoAdapter(private val eventos: List<Evento>) : RecyclerView.Adapter<EventoAdapter.EventoViewHolder>() {

    class EventoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.text_titulo_evento)
        val data: TextView = view.findViewById(R.id.text_data_evento)
        val local: TextView = view.findViewById(R.id.text_local_evento)
        val imagem: ImageView = view.findViewById(R.id.image_evento)
        val iconeFavorito: ImageView = view.findViewById(R.id.icon_favorito)
        val preco: TextView = view.findViewById(R.id.text_preco_evento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_evento_vertical, parent, false)
        return EventoViewHolder(view)
    }

    override fun getItemCount() = eventos.size

    private fun formatarData(dataIso: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd 'de' MMMM '•' HH:mm", Locale("pt", "BR"))
        return try {
            val parsedDate = parser.parse(dataIso)
            if (parsedDate != null) formatter.format(parsedDate) else dataIso
        } catch (e: Exception) {
            dataIso
        }
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = eventos[position]
        val context = holder.itemView.context

        holder.nome.text = evento.nome
        holder.local.text = evento.local
        holder.data.text = formatarData(evento.data)

        if (evento.preco > 0.0) {
            holder.preco.text = "R\$ ${String.format(Locale("pt", "BR"), "%.2f", evento.preco)}"
        } else {
            holder.preco.text = "Grátis"
        }

        Glide.with(context)
            .load(evento.imagemUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(holder.imagem)

        val corFavorito = if (evento.isFavorito) {
            ContextCompat.getColor(context, R.color.accent_brand)
        } else {
            ContextCompat.getColor(context, R.color.white)
        }
        holder.iconeFavorito.setColorFilter(corFavorito)

        holder.iconeFavorito.setOnClickListener {
            evento.isFavorito = !evento.isFavorito
            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetalhesEventoActivity::class.java)
            intent.putExtra("EXTRA_EVENTO", evento)
            context.startActivity(intent)
        }
    }
}