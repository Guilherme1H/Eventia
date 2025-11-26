package com.example.eventia

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.eventia.databinding.ActivityCompraConcluidaBinding

class CompraConcluidaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompraConcluidaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompraConcluidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonVerIngressos.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("NAVIGATE_TO", "PROFILE")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
}