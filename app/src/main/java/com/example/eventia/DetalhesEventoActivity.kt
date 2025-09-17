package com.example.eventia

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Locale

class DetalhesEventoActivity : AppCompatActivity() {

    private fun formatarData(dataIso: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
            val formatter = SimpleDateFormat("dd 'de' MMMM 'de' yyyy 'às' HH:mm", Locale("pt", "BR"))
            formatter.format(parser.parse(dataIso)!!)
        } catch (e: Exception) {
            dataIso
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_evento)

        val backButton = findViewById<ImageButton>(R.id.backButtonDetalhes)
        backButton.setOnClickListener {
            finish()
        }

        val evento = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_EVENTO", Evento::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Evento>("EXTRA_EVENTO")
        }

        if (evento != null) {
            Log.d("DetalhesActivity", "URL da imagem para carregar: ${evento.imagemUrl}")

            val imageView = findViewById<ImageView>(R.id.image_evento_detalhe)

            Glide.with(this)
                .load(evento.imagemUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imageView)

            findViewById<TextView>(R.id.text_nome_evento_detalhe).text = evento.nome
            findViewById<TextView>(R.id.text_local_evento_detalhe).text = evento.local
            findViewById<TextView>(R.id.text_descricao_evento).text = evento.descricao
            findViewById<TextView>(R.id.text_data_evento_detalhe).text = formatarData(evento.data)

            val precoTextView = findViewById<TextView>(R.id.text_preco_evento_detalhe)
            if (evento.preco > 0.0) {
                precoTextView.text = "R\$ ${"%.2f".format(evento.preco).replace('.', ',')}"
            } else {
                precoTextView.text = "Grátis"
            }

            val comprarButton = findViewById<MaterialButton>(R.id.button_comprar_ingresso)
            comprarButton.setOnClickListener {
                Toast.makeText(this, "Funcionalidade de compra ainda não implementada.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("DetalhesActivity", "ERRO CRÍTICO: O objeto Evento chegou NULO na tela de detalhes.")
        }
    }
}