package com.example.eventia

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IngressoManager(context: Context, userId: String) {

    private val prefs = context.getSharedPreferences("IngressoPrefs_User_$userId", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun adicionarIngressoComprado(ingresso: IngressoComprado) {
        val ingressos = getIngressosComprados().toMutableList()
        ingressos.add(ingresso)
        salvarIngressos(ingressos)
    }

    fun getIngressosComprados(): List<IngressoComprado> {
        val json = prefs.getString("lista_ingressos", "[]")
        val type = object : TypeToken<List<IngressoComprado>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun getIngressosCompradosIds(): Set<String> {
        return getIngressosComprados().map { it.eventoId.toString() }.toSet()
    }

    private fun salvarIngressos(ingressos: List<IngressoComprado>) {
        val json = gson.toJson(ingressos)
        prefs.edit().putString("lista_ingressos", json).apply()
    }
}