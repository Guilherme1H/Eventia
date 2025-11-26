package com.example.eventia

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CarrinhoManager(context: Context, private val userId: String) {

    private val prefs = context.getSharedPreferences("CarrinhoPrefs_User_$userId", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val keyItens = "itens_carrinho"

    fun adicionarItem(evento: Evento, quantidade: Int) {
        val itens = getItens().toMutableList()
        val itemExistente = itens.find { it.evento.id == evento.id }
        if (itemExistente != null) {
            itemExistente.quantidade += quantidade
        } else {
            itens.add(ItemCarrinho(evento, quantidade))
        }
        salvarItens(itens)
        Log.d("CarrinhoManager", "Item '${evento.nome}' adicionado/atualizado para o usuário $userId.")
    }

    fun aumentarQuantidade(eventoId: Int) {
        val itens = getItens().toMutableList()
        itens.find { it.evento.id == eventoId }?.let { it.quantidade++ }
        salvarItens(itens)
        Log.d("CarrinhoManager", "Aumentou quantidade para o item $eventoId.")
    }

    fun diminuirQuantidade(eventoId: Int) {
        val itens = getItens().toMutableList()
        val item = itens.find { it.evento.id == eventoId }
        item?.let {
            if (it.quantidade > 1) {
                it.quantidade--
                salvarItens(itens)
                Log.d("CarrinhoManager", "Diminuiu quantidade para o item $eventoId.")
            } else {
                removerItemPorId(eventoId)
            }
        }
    }

    fun removerItemPorId(eventoId: Int) {
        val itens = getItens().toMutableList()
        val foiRemovido = itens.removeAll { it.evento.id == eventoId }
        if (foiRemovido) {
            salvarItens(itens)
            Log.d("CarrinhoManager", "Item com ID $eventoId removido do carrinho.")
        }
    }

    fun getItens(): List<ItemCarrinho> {
        val json = prefs.getString(keyItens, "[]")
        val type = object : TypeToken<List<ItemCarrinho>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun getValorTotal(): Double {
        return getItens().sumOf { it.evento.preco * it.quantidade }
    }

    fun limparCarrinho() {
        prefs.edit().remove(keyItens).apply()
        Log.d("CarrinhoManager", "Carrinho do usuário $userId foi limpo.")
    }

    private fun salvarItens(itens: List<ItemCarrinho>) {
        val json = gson.toJson(itens)
        prefs.edit().putString(keyItens, json).apply()
    }
}