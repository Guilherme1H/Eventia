package com.example.eventia

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.eventia.databinding.ActivityFormularioEventoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class FormularioEventoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioEventoBinding
    private val calendar = Calendar.getInstance()

    // --- Variáveis do Firebase ---
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private lateinit var auth: FirebaseAuth

    // --- Variáveis de UI e Dados ---
    private var selectedImageUri: Uri? = null
    private lateinit var progressDialog: AlertDialog

    // Launcher para selecionar imagem da galeria
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            // Carrega a imagem selecionada no ImageView usando a biblioteca Glide
            Glide.with(this).load(it).into(binding.imageViewFormulario)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Inicialização do Firebase ---
        database = Firebase.database.reference
        storage = Firebase.storage.reference
        auth = FirebaseAuth.getInstance()

        // Configura a Toolbar
        setSupportActionBar(binding.toolbarFormulario)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarFormulario.setNavigationOnClickListener { finish() }

        // Configura o diálogo de progresso (loading)
        setupProgressDialog()

        // Configura os cliques dos botões
        setupClickListeners()
    }

    private fun setupProgressDialog() {
        val builder = AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(R.layout.dialog_progress) // Você precisa criar este layout simples
        progressDialog = builder.create()
    }

    private fun setupClickListeners() {
        binding.editTextData.setOnClickListener { showDatePickerDialog() }
        binding.buttonSelecionarImagem.setOnClickListener { imagePickerLauncher.launch("image/*") }
        binding.buttonSalvarEvento.setOnClickListener { salvarEvento() }
    }

    private fun salvarEvento() {
        // 1. VERIFICAÇÃO DE AUTENTICAÇÃO (A CORREÇÃO MAIS IMPORTANTE)
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "Erro de autenticação! Faça o login novamente.", Toast.LENGTH_LONG).show()
            return // Para a execução aqui se não houver usuário
        }

        // 2. Validação dos campos do formulário
        val nome = binding.editTextNome.text.toString().trim() // Use o ID correto do seu XML
        val local = binding.editTextLocal.text.toString().trim()
        val precoStr = binding.editTextPreco.text.toString().trim()
        val data = binding.editTextData.text.toString().trim()

        if (selectedImageUri == null) {
            Toast.makeText(this, "Por favor, selecione uma imagem.", Toast.LENGTH_SHORT).show()
            return
        }
        if (nome.isEmpty() || local.isEmpty() || precoStr.isEmpty() || data.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }
        val preco = precoStr.toDoubleOrNull()
        if (preco == null) {
            Toast.makeText(this, "Por favor, insira um preço válido.", Toast.LENGTH_SHORT).show()
            return
        }

        // Mostra o loading e desabilita o botão
        progressDialog.show()
        binding.buttonSalvarEvento.isEnabled = false

        // 3. Upload da imagem para o Firebase Storage
        val filename = UUID.randomUUID().toString()
        val imageRef = storage.child("imagens_eventos/$filename.jpg")

        imageRef.putFile(selectedImageUri!!)
            .addOnSuccessListener {
                // 4. Imagem enviada com sucesso, agora pegamos a URL dela
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    // 5. Agora que temos a URL, salvamos tudo no Realtime Database
                    salvarDadosDoEventoNoDatabase(nome, data, local, preco, downloadUri.toString())
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro ao obter URL da imagem: ${exception.message}", Toast.LENGTH_LONG).show()
                    hideProgressAndResetButton()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao fazer upload da imagem: ${exception.message}", Toast.LENGTH_LONG).show()
                hideProgressAndResetButton()
            }
    }

    private fun salvarDadosDoEventoNoDatabase(nome: String, data: String, local: String, preco: Double, imageUrl: String) {
        val eventoId = database.child("eventos").push().key
        if (eventoId == null) {
            Toast.makeText(this, "Erro ao criar ID para o evento.", Toast.LENGTH_SHORT).show()
            hideProgressAndResetButton()
            return
        }

        val evento = Evento(
            id = eventoId,
            nome = nome,
            data = data,
            local = local,
            preco = preco,
            imagemUrl = imageUrl
        )

        database.child("eventos").child(eventoId).setValue(evento)
            .addOnSuccessListener {
                // 6. Sucesso!
                progressDialog.dismiss()
                Toast.makeText(this, "Evento salvo com sucesso!", Toast.LENGTH_SHORT).show()
                finish() // Fecha a tela e volta para a lista
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao salvar dados do evento: ${exception.message}", Toast.LENGTH_LONG).show()
                hideProgressAndResetButton()
            }
    }

    private fun hideProgressAndResetButton() {
        progressDialog.dismiss()
        binding.buttonSalvarEvento.isEnabled = true
    }

    // Funções de Date e Time Picker (sem alterações)
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(this, { _, y, m, d -> calendar.set(y, m, d); showTimePickerDialog() }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(this, { _, h, m -> calendar.set(Calendar.HOUR_OF_DAY, h); calendar.set(Calendar.MINUTE, m); updateDataInView() }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        timePickerDialog.show()
    }

    private fun updateDataInView() {
        val myFormat = "dd/MM/yyyy HH:mm"
        val sdf = SimpleDateFormat(myFormat, Locale("pt", "BR"))
        binding.editTextData.setText(sdf.format(calendar.time))
    }
}