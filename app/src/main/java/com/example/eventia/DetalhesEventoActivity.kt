package com.example.eventia

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.eventia.databinding.ActivityDetalhesEventoBinding
import java.text.NumberFormat
import java.util.Locale

class DetalhesEventoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalhesEventoBinding
    private var evento: Evento? = null
    private var quantidade = 1

    private lateinit var carrinhoManager: CarrinhoManager
    private lateinit var ingressoManager: IngressoManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalhesEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        evento = intent.getParcelableExtra<Evento>("EXTRA_EVENTO")

        if (evento == null) {
            Toast.makeText(this, "Erro ao carregar evento.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val userId = SessionManager.getUserId(this)
        carrinhoManager = CarrinhoManager(this, userId)
        ingressoManager = IngressoManager(this, userId)

        setupUI()
        setupClickListeners()
        checkTicketStatus()
    }

    private fun checkTicketStatus() {
        evento?.let {
            val isPurchased = ingressoManager.getIngressosCompradosIds().contains(it.id.toString())
            if (isPurchased) {
                binding.quantitySelector.isVisible = false
                binding.buttonAdicionarCarrinho.isVisible = false
                binding.layoutIngressoAdquirido.isVisible = true
            } else {
                binding.quantitySelector.isVisible = true
                binding.buttonAdicionarCarrinho.isVisible = true
                binding.layoutIngressoAdquirido.isVisible = false
            }
        }
    }

    private fun setupUI() {
        evento?.let {
            Glide.with(this)
                .load(it.imagemUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(binding.imageEventoDetalhe)

            binding.textNomeEventoDetalhe.text = it.nome
            binding.textDescricaoEvento.text = it.descricao
            binding.textLocalEventoDetalhe.text = it.local
            binding.textDataEventoDetalhe.text = it.data

            val formatadorMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            binding.textPrecoEventoDetalhe.text = formatadorMoeda.format(it.preco)

            // Atenção: O ID do seu TextView de quantidade no XML era text_view_quantity
            binding.textViewQuantity.text = quantidade.toString()
        }
    }

    private fun setupClickListeners() {
        binding.backButtonDetalhes.setOnClickListener {
            finish()
        }

        binding.buttonMinus.setOnClickListener {
            if (quantidade > 1) {
                quantidade--
                binding.textViewQuantity.text = quantidade.toString()
            }
        }

        binding.buttonPlus.setOnClickListener {
            quantidade++
            binding.textViewQuantity.text = quantidade.toString()
        }

        binding.buttonAdicionarCarrinho.setOnClickListener {
            evento?.let { evt ->
                carrinhoManager.adicionarItem(evt, quantidade)
                Toast.makeText(this, "'${evt.nome}' adicionado ao carrinho!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}