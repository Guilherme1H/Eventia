package com.example.eventia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventia.databinding.ActivityVisualizacaoIngressoBinding
import com.example.eventia.utils.DateUtils
import java.util.Locale

class VisualizacaoIngressoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVisualizacaoIngressoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVisualizacaoIngressoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ingresso = intent.getParcelableExtra<IngressoComprado>("EXTRA_INGRESSO")

        binding.toolbarIngresso.setNavigationOnClickListener { finish() }

        if (ingresso != null) {
            setupUI(ingresso)
        } else {
            finish()
        }
    }

    private fun setupUI(ingresso: IngressoComprado) {
        binding.textNomeIngresso.text = ingresso.nomeEvento
        binding.textLocalIngresso.text = "Local: ${ingresso.localEvento}"

        val dataFormatada = DateUtils.formatarDataCompleta(ingresso.dataEvento)
        binding.textDataIngresso.text = dataFormatada

        binding.textIdIngresso.text = "ID do Ingresso: ${ingresso.id.substring(0, 8).uppercase(Locale.ROOT)}"
        binding.textQuantidade.text = "Quantidade: ${ingresso.quantidade}"
    }
}