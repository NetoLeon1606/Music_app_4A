package com.example.music_app4a

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class AlbumDetailActivity : AppCompatActivity() {

    private lateinit var imgPortadaDetalle: ImageView
    private lateinit var tvNombreAlbumDetalle: TextView
    private lateinit var tvArtistaAlbum: TextView
    private lateinit var tvAlbumDetalle: TextView
    private lateinit var btnFavorito: ImageView

    private lateinit var dbHelper: DatabaseHelper
    private var albumFavorito = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_album_detail)

        dbHelper = DatabaseHelper(this)

        imgPortadaDetalle = findViewById(R.id.imgPortadaDetalle)
        tvNombreAlbumDetalle = findViewById(R.id.tvNombreAlbumDetalle)
        tvArtistaAlbum = findViewById(R.id.tvArtistaAlbumDetalle)
        tvAlbumDetalle = findViewById(R.id.tvAlbumDetalle)
        btnFavorito = findViewById(R.id.btnFavorito)

        mostrarInformacionAlbum()

    }

    private fun mostrarInformacionAlbum() {
        val idAlbum = intent.getLongExtra("ID_ALBUM", -1L)
        val nombreAlbum = intent.getStringExtra("NOMBRE_ALBUM") ?: "Album Desconocido"
        val artista = intent.getStringExtra("ARTISTA_ALBUM") ?: "Artista Desconocido"
        val genero = intent.getStringExtra("GENERO_ALBUM") ?: "Genero Desconocido"
        val numeroCanciones = intent.getIntExtra("CANCIONES_ALBUM", 0)
        val fecha = intent.getStringExtra("FECHA_ALBUM") ?: "Sin Fecha"
        val imagen = intent.getStringExtra("IMAGEN_ALBUM") ?: ""

        tvNombreAlbumDetalle.text = nombreAlbum
        tvArtistaAlbum.text = artista
        tvAlbumDetalle.text = "$genero - $numeroCanciones canciones"

        Glide.with(this)
            .load(imagen)
            .centerCrop()
            .into(imgPortadaDetalle)

        // Verificar si el album ya esta guardado en SQLite y actualizar icono inicial
        if (idAlbum != -1L) {
            albumFavorito = dbHelper.existeAlbumFavorito(idAlbum)
            if (albumFavorito) {
                btnFavorito.setImageResource(R.drawable.favorito)
            } else {
                btnFavorito.setImageResource(R.drawable.no_favorite)
            }
        }

        btnFavorito.setOnClickListener {
            if (albumFavorito) {
                val eliminado = dbHelper.eliminarAlbumFavorito(idAlbum)
                if (eliminado) {
                    albumFavorito = false
                    btnFavorito.setImageResource(R.drawable.no_favorite)
                    Toast.makeText(this, "Album $nombreAlbum eliminado de favoritos", Toast.LENGTH_SHORT).show()
                }
            } else {
                val guardado = dbHelper.guardarAlbumFavorito(
                    albumId = idAlbum,
                    nombreAlbum = nombreAlbum,
                    artista = artista,
                    genero = genero,
                    canciones = numeroCanciones,
                    fecha = fecha,
                    imagen = imagen
                )
                if (guardado) {
                    albumFavorito = true
                    btnFavorito.setImageResource(R.drawable.favorito)
                    Toast.makeText(this, "Album $nombreAlbum agregado a favoritos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}