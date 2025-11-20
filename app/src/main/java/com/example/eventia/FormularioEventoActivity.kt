package com.example.eventia

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import android.app.Activity


class FormularioEventoActivity : AppCompatActivity() {

    private lateinit var editTextNome: EditText
    private lateinit var editTextData: EditText
    private lateinit var editTextLocal: EditText
    private lateinit var editTextPreco: EditText
    private lateinit var editTextDescricao: EditText
    private lateinit var editTextUrlImagem: EditText
    private lateinit var buttonSalvarEvento: Button
    private lateinit var toolbar: Toolbar

    private val calendar = Calendar.getInstance()
    private var eventoParaEditar: Evento? = null
    private lateinit var progressDialog: AlertDialog
    private val apiService by lazy { RetrofitClient.instance.create(ApiService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_evento)

        bindViews()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        setupProgressDialog()
        setupClickListeners()

        eventoParaEditar = intent.getParcelableExtra("EVENTO_PARA_EDITAR")
        if (eventoParaEditar != null) {
            preencherFormularioParaEdicao()
        } else {
            supportActionBar?.title = "Novo Evento"
        }
    }

    private fun bindViews() {
        toolbar = findViewById(R.id.toolbar_formulario)
        editTextNome = findViewById(R.id.edit_text_nome)
        editTextData = findViewById(R.id.edit_text_data)
        editTextLocal = findViewById(R.id.edit_text_local)
        editTextPreco = findViewById(R.id.edit_text_preco)
        editTextDescricao = findViewById(R.id.edit_text_descricao)
        editTextUrlImagem = findViewById(R.id.edit_text_url_imagem)
        buttonSalvarEvento = findViewById(R.id.button_salvar_evento)
    }

    private fun preencherFormularioParaEdicao() {
        supportActionBar?.title = "Editar Evento"
        eventoParaEditar?.let { evento ->
            editTextNome.setText(evento.nome)
            editTextLocal.setText(evento.local)
            editTextDescricao.setText(evento.descricao)
            editTextPreco.setText(evento.preco.toString())
            editTextUrlImagem.setText(evento.imagemUrl)

            try {
                val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date = parser.parse(evento.data)
                if (date != null) {
                    calendar.time = date
                    updateDataInView()
                }
            } catch (e: Exception) {
                editTextData.setText(evento.data)
            }
        }
    }

    private fun setupClickListeners() {
        editTextData.setOnClickListener { showDatePickerDialog() }
        buttonSalvarEvento.setOnClickListener { dispatchSave() }
    }

    private fun dispatchSave() {
        if (!areFieldsValid()) {
            Toast.makeText(this, "Por favor, preencha todos os campos obrigatórios.", Toast.LENGTH_SHORT).show()
            return
        }

        if (eventoParaEditar != null) {
            atualizarEvento()
        } else {
            criarEvento()
        }
    }

    private fun criarEvento() {
        progressDialog.show()
        buttonSalvarEvento.isEnabled = false

        val nome = editTextNome.text.toString()
        val data = editTextData.text.toString()
        val local = editTextLocal.text.toString()
        val preco = editTextPreco.text.toString()
        val descricao = editTextDescricao.text.toString()
        val imagemUrl = editTextUrlImagem.text.toString()
        val idUsuario = 1

        apiService.criarEvento(nome, data, local, preco, descricao, imagemUrl, idUsuario).enqueue(object: Callback<RegistrationResponse> {
            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                handleResponse(response, "Evento criado com sucesso!")
            }
            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                handleFailure(t)
            }
        })
    }

    private fun atualizarEvento() {
        progressDialog.show()
        buttonSalvarEvento.isEnabled = false

        val id = eventoParaEditar!!.id
        val nome = editTextNome.text.toString()
        val data = editTextData.text.toString()
        val local = editTextLocal.text.toString()
        val preco = editTextPreco.text.toString()
        val descricao = editTextDescricao.text.toString()
        val imagemUrl = editTextUrlImagem.text.toString()

        apiService.atualizarEvento(id, nome, data, local, preco, descricao, imagemUrl).enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                handleResponse(response, "Evento atualizado com sucesso!")
            }
            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                handleFailure(t)
            }
        })
    }

    private fun areFieldsValid(): Boolean {
        return editTextNome.text.isNotBlank() &&
                editTextLocal.text.isNotBlank() &&
                editTextPreco.text.isNotBlank() &&
                editTextData.text.isNotBlank() &&
                editTextDescricao.text.isNotBlank()
    }

    private fun handleResponse(response: Response<RegistrationResponse>, successMessage: String) {
        hideProgressAndResetButton()
        if (response.isSuccessful && response.body()?.success == true) {
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            val errorMsg = response.body()?.message ?: "Erro desconhecido do servidor."
            Toast.makeText(this, "Erro: $errorMsg", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleFailure(t: Throwable) {
        hideProgressAndResetButton()
        Toast.makeText(this, "Falha na conexão: ${t.message}", Toast.LENGTH_LONG).show()
    }

    private fun setupProgressDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        builder.setView(inflater.inflate(R.layout.dialog_progress, null))
        builder.setCancelable(false)
        progressDialog = builder.create()
    }

    private fun hideProgressAndResetButton() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
        buttonSalvarEvento.isEnabled = true
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            calendar.set(Calendar.YEAR, selectedYear)
            calendar.set(Calendar.MONTH, selectedMonth)
            calendar.set(Calendar.DAY_OF_MONTH, selectedDay)
            showTimePickerDialog()
        }, year, month, day).show()
    }

    private fun showTimePickerDialog() {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            calendar.set(Calendar.MINUTE, selectedMinute)
            updateDataInView()
        }, hour, minute, true).show()
    }

    private fun updateDataInView() {
        val myFormat = "dd/MM/yyyy HH:mm"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        editTextData.setText(sdf.format(calendar.time))
    }
}