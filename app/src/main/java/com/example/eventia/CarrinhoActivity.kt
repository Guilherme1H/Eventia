package com.example.eventia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventia.databinding.ActivityCarrinhoBinding
import java.text.NumberFormat
import java.util.Locale

class CarrinhoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarrinhoBinding
    private lateinit var carrinhoAdapter: CarrinhoAdapter
    private lateinit var carrinhoManager: CarrinhoManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarrinhoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = SessionManager.getUserId(this)
        carrinhoManager = CarrinhoManager(this, userId)

        setupRecyclerView()
        setupClickListeners()
        calcularEAtualizarTotal()
        verificarEstadoCarrinho()
    }

    override fun onResume() {
        super.onResume()
        val itensAtualizados = carrinhoManager.getItens().toMutableList()
        if (::carrinhoAdapter.isInitialized) {
            setupRecyclerView()
        } else {
            setupRecyclerView()
        }
        calcularEAtualizarTotal()
        verificarEstadoCarrinho()
    }

    private fun setupRecyclerView() {
        val itensDoCarrinho = carrinhoManager.getItens().toMutableList()

        carrinhoAdapter = CarrinhoAdapter(itensDoCarrinho, carrinhoManager) {
            calcularEAtualizarTotal()
            verificarEstadoCarrinho()
        }

        binding.recyclerViewCarrinho.apply {
            adapter = carrinhoAdapter
            layoutManager = LinearLayoutManager(this@CarrinhoActivity)
        }
    }

    private fun calcularEAtualizarTotal() {
        val total = carrinhoManager.getValorTotal()
        val formatadorMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        binding.textTotalCarrinho.text = formatadorMoeda.format(total)
    }
    private fun verificarEstadoCarrinho() {
        val itens = carrinhoManager.getItens()
        if (itens.isEmpty()) {
            binding.grupoConteudoCarrinho.visibility = View.GONE
            binding.layoutCarrinhoVazio.visibility = View.VISIBLE
        } else {
            binding.grupoConteudoCarrinho.visibility = View.VISIBLE
            binding.layoutCarrinhoVazio.visibility = View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.toolbarCarrinho.setNavigationOnClickListener {
            finish()
        }

        binding.buttonFinalizarCompra.setOnClickListener {
            val itens = carrinhoManager.getItens()
            if (itens.isNotEmpty()) {
                val intent = Intent(this, PagamentoActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Seu carrinho está vazio.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonExplorarEventos.setOnClickListener {
            finish()
        }
    }
}