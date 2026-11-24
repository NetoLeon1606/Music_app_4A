package com.example.music_app4a

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var auth : FirebaseAuth
    private lateinit var credentialManager : CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorites)

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        auth = FirebaseAuth.getInstance()
        credentialManager = CredentialManager.create(this)

        menuNavegacion()

    }

    // Metodo para el funcionamiento del menu
    private fun menuNavegacion(){

        // Mostrar el elemento seleccionado
        bottomNavigationView.selectedItemId = R.id.nav_inicio

        // Funcion de las opciones
        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_inicio ->{
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_favoritos -> {
                    true
                }
                R.id.nav_salir -> {
                    cerrarSesionGoogle()
                    true
                }
                else -> false
            }
        }
    }

    // Metodo para cerrar sesion
    private fun cerrarSesionGoogle(){
        auth.signOut()

        lifecycleScope.launch{
            try {

                // Limpiar las credenciales del usuario
                credentialManager.clearCredentialState(
                    androidx.credentials.ClearCredentialStateRequest()
                )

            } catch (e: Exception){
                Toast.makeText(this@FavoritesActivity, "Error: ${e.toString()}", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this@FavoritesActivity, MainActivity::class.java)

            // Limpiar toda la informacion e iniciar la pantalla totalmente desde cero
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}