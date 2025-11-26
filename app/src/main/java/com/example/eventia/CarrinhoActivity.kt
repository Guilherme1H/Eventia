package com.example.eventia

import android.content.Intent
import android.os.Bundle
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
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
        calcularEAtualizarTotal()
    }

    private fun setupRecyclerView() {
        val itensDoCarrinho = carrinhoManager.getItens().toMutableList()

        carrinhoAdapter = CarrinhoAdapter(itensDoCarrinho, carrinhoManager) {
            calcularEAtualizarTotal()
        }

        binding.recyclerViewCarrinho.apply {
            adapter = carrinhoAdapter
            layoutManager = LinearLayoutManager(this@CarrinhoActivity)
        }
    }

    private fun calcularEAtualizarTotal() {
        val total = carrinhoManager.getValorTotal()
        val formatadorMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        binding.textTotalValor.text = formatadorMoeda.format(total)
    }

    private fun setupClickListeners() {
        binding.toolbarCarrinho.setNavigationOnClickListener {
            finish()
        }

        binding.buttonFinalizarCompra.setOnClickListener {
            val intent = Intent(this, PagamentoActivity::class.java)
            startActivity(intent)
        }
    }
}