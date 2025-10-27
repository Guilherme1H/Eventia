package com.example.eventia

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

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
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
            val formatter = SimpleDateFormat("dd 'de' MMMM '•' HH:mm", Locale("pt", "BR"))
            formatter.format(parser.parse(dataIso)!!)
        } catch (e: Exception) {
            dataIso
        }
    }

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = eventos[position]

        holder.nome.text = evento.nome
        holder.local.text = evento.local
        holder.data.text = formatarData(evento.data)

        // Sua lógica de preço, mantida 100%!
        if (evento.preco > 0.0) {
            holder.preco.text = "R\$ ${"%.2f".format(evento.preco).replace('.', ',')}"
        } else {
            holder.preco.text = "Grátis"
        }

        // Sua lógica do Glide, mantida 100%!
        Glide.with(holder.itemView.context)
            .load(evento.imagemUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(holder.imagem)

        if (evento.isFavorito) {
            holder.iconeFavorito.setColorFilter(holder.itemView.context.getColor(R.color.accent_brand))
        } else {
            holder.iconeFavorito.setColorFilter(holder.itemView.context.getColor(R.color.white))
        }

        holder.iconeFavorito.setOnClickListener {
            evento.isFavorito = !evento.isFavorito
            notifyItemChanged(position)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalhesEventoActivity::class.java)
            intent.putExtra("EXTRA_EVENTO", evento)
            context.startActivity(intent)
        }
    }
}