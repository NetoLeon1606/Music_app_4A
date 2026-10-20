package com.example.music_app4a

import android.content.Intent
import android.os.Bundle
import android.view.TextureView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var etCorreo : EditText
    private lateinit var etpass : EditText
    private lateinit var btnInicio : Button
    private lateinit var dbHelper : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val tvCrearCuenta = findViewById<TextView>(R.id.tvCrearCuenta)

        etCorreo = findViewById(R.id.etCorreo)
        etpass = findViewById(R.id.etPassword)
        btnInicio = findViewById(R.id.btnIngresar)
        dbHelper = DatabaseHelper(this)

        // Evento para Iniciar Sesion
        btnInicio.setOnClickListener {
            // Todo: Agregar Metodo de Inicio de Sesion
        }



        tvCrearCuenta.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}