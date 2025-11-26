package com.example.eventia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventia.databinding.ItemCarrinhoBinding
import java.text.NumberFormat
import java.util.Locale

class CarrinhoAdapter(
    private var itens: MutableList<ItemCarrinho>,
    private val carrinhoManager: CarrinhoManager,
    private val onItemChanged: () -> Unit
) : RecyclerView.Adapter<CarrinhoAdapter.CarrinhoViewHolder>() {

    inner class CarrinhoViewHolder(val binding: ItemCarrinhoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarrinhoViewHolder {
        val binding = ItemCarrinhoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarrinhoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarrinhoViewHolder, position: Int) {
        val item = itens[position]
        val formatadorMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        holder.binding.apply {
            textNomeEventoCarrinho.text = item.evento.nome
            textViewQuantityCarrinho.text = item.quantidade.toString()
            textPrecoUnitarioCarrinho.text = formatadorMoeda.format(item.evento.preco)

            Glide.with(holder.itemView.context)
                .load(item.evento.imagemUrl) // Versão correta
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(imageEventoCarrinho)

            buttonPlusCarrinho.setOnClickListener {
                carrinhoManager.aumentarQuantidade(item.evento.id)
                item.quantidade++
                notifyItemChanged(position)
                onItemChanged()
            }

            buttonMinusCarrinho.setOnClickListener {
                if (item.quantidade == 1) {
                    carrinhoManager.diminuirQuantidade(item.evento.id)
                    itens.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itens.size)
                } else {
                    carrinhoManager.diminuirQuantidade(item.evento.id)
                    item.quantidade--
                    notifyItemChanged(position)
                }
                onItemChanged()
            }
        }
    }

    override fun getItemCount(): Int = itens.size
}