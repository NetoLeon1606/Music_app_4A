package com.example.music_app4a

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class AlbumDetailActivity : AppCompatActivity() {

    companion object{

        private const val CANAL_ID = "canal_general"
        private const val NOTIFICATION_ID = 1
        private const val CODIGO_PERMISO = 100

    }

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

        imgPortadaDetalle = findViewById(R.id.imgPortadaDetalle)
        tvNombreAlbumDetalle = findViewById(R.id.tvNombreAlbumDetalle)
        tvArtistaAlbum = findViewById(R.id.tvArtistaAlbumDetalle)
        tvAlbumDetalle = findViewById(R.id.tvAlbumDetalle)
        btnFavorito = findViewById(R.id.btnFavorito)

        dbHelper = DatabaseHelper(this)

        mostrarInformacionAlbum()

        crearCanalNoti()

        findViewById<View>(R.id.main)?.let { vistaPrincipal ->
            ViewCompat.setOnApplyWindowInsetsListener(vistaPrincipal) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
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

    // 1. Metodo para crear el canal
    private fun crearCanalNoti() {
        // Verificar que el SDK sea Android 8.0 (Oreo) o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                // Crear el canal val canal
                CANAL_ID,
                "Notificaciones generales",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            canal.description = "Canal para notificaciones de album"

            val adminNotificaciones = getSystemService(NotificationManager::class.java)
            adminNotificaciones.createNotificationChannel(canal)
        }
    }

    // 2. Metodo para crear y mostrar la notificacion
    private fun mostrarNoti() {
        // Verificar permiso en Android 13 (Tiramisu) o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Preguntar al usuario por el permiso de notificaciones
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                CODIGO_PERMISO
            )
            Toast.makeText(this, "Autoriza el permiso y vuelve a intentar", Toast.LENGTH_SHORT).show()
            return
        }
        // Crear la notificacion y su diseño de esta
        val notificacion = NotificationCompat.Builder(this, CANAL_ID)
            .setSmallIcon(R.drawable.baseline_email_24)
            .setContentTitle("Music App")
            .setContentText("Un album fue agregado a la base de datos")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat
            .from(this)
            .notify(NOTIFICATION_ID,
                notificacion)
    }

}