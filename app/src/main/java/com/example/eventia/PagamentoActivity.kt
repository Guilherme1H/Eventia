package com.example.eventia

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eventia.databinding.ActivityPagamentoBinding
import java.util.UUID

class PagamentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPagamentoBinding
    private lateinit var carrinhoManager: CarrinhoManager
    private lateinit var ingressoManager: IngressoManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = SessionManager.getUserId(this)
        carrinhoManager = CarrinhoManager(this, userId)
        ingressoManager = IngressoManager(this, userId)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.toolbarPagamento.setNavigationOnClickListener {
            finish()
        }

        binding.buttonPagarAgora.setOnClickListener {
            if (isFormularioValido()) {
                processarPagamento()
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isFormularioValido(): Boolean {
        return binding.editTextNomeCartao.text.toString().isNotBlank() &&
                binding.editTextNumeroCartao.text.toString().isNotBlank() &&
                binding.editTextValidade.text.toString().isNotBlank() &&
                binding.editTextCvv.text.toString().isNotBlank()
    }

    private fun processarPagamento() {
        val itensNoCarrinho = carrinhoManager.getItens()

        if (itensNoCarrinho.isEmpty()) {
            Toast.makeText(this, "Seu carrinho está vazio.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        for (item in itensNoCarrinho) {
            val ingressoComprado = IngressoComprado(
                id = UUID.randomUUID().toString(),
                eventoId = item.evento.id,
                nomeEvento = item.evento.nome,
                dataEvento = item.evento.data,
                localEvento = item.evento.local,
                imagemUrl = item.evento.imagemUrl,
                quantidade = item.quantidade
            )
            ingressoManager.adicionarIngressoComprado(ingressoComprado)
        }

        carrinhoManager.limparCarrinho()

        Toast.makeText(this, "Pagamento processado com sucesso!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, CompraConcluidaActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}