package com.example.music_app4a

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.music_app4a.model.Album
import com.example.music_app4a.model.ITunesResponse
import com.example.music_app4a.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class HomeActivity : AppCompatActivity() {

    private lateinit var tvEstado : TextView
    private lateinit var contenedorRock : LinearLayout
    private lateinit var contenedorIndie : LinearLayout
    private lateinit var contenedorEnjambre : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        tvEstado = findViewById(R.id.tvEstado)
        contenedorRock = findViewById(R.id.contenedorRock)
        contenedorIndie = findViewById(R.id.contenedorIndie)
        contenedorEnjambre = findViewById(R.id.contenedorEnjambre)

        obtenerAlbum("Rock", contenedorRock)
        obtenerAlbum("indie", contenedorIndie)
        obtenerAlbum("Enjambre", contenedorEnjambre)
    }

    // Metodo para cargar los albumnes
    private fun obtenerAlbum(genero : String, contenedor: LinearLayout){

        RetrofitClient.api.obtenerAlbumGenero(
            genero = genero,
            pais = "MX",
            media ="music",
            entidad = "album",
            limite = 10,
        ).enqueue(object : Callback<ITunesResponse> {

            // Metodos obligatorios para el funcionanmiento
            // Metodo para verificar que la respuesta sea correcta
            override fun onResponse(call: Call<ITunesResponse?>, response: Response<ITunesResponse?>) {

                // Variable que obtenga los resultados del API
                val albumes = response.body()?.results ?: emptyList()

                // Si la lista de album no esta vacia, se manda a llamar a los item_album
                if(albumes.isNotEmpty()){
                    // Desaparecer el elemento que indica la carga del API
                    tvEstado.visibility = View.GONE
                    mostrarAlbumes(albumes, contenedor)

                }else {
                    Toast.makeText(this@HomeActivity, "No hay Resultados", Toast.LENGTH_SHORT).show()

                }
            }

            // Metodo para verificar si no hay respuesta
            override fun onFailure(call: Call<ITunesResponse?>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Error en el Api", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Metodo para mostrar un listado de Albumes

    private fun mostrarAlbumes(albumes : List<Album>, contenedor : LinearLayout){

        // Evitar resultados duplicados
        contenedor.removeAllViews()

        // Ciclo para recorrer la informacion de la respuesta del API
        for(album in albumes){

            // Indicar la vista en la que se va a mostrar la info
            // (item_album)
            val vistaAlbum = layoutInflater.inflate(
                R.layout.item_album,
                contenedor,
                false
            )

            val imgPortada = vistaAlbum.findViewById<ImageView>(R.id.imgPortadaAlbum)
            val tvNombreAlbum = vistaAlbum.findViewById<TextView>(R.id.tvNombreAlbum)
            val tvNombreArtista = vistaAlbum.findViewById<TextView>(R.id.tvNombreArtista)

            tvNombreAlbum.text = album.collectionName ?: "Album sin nombre"
            tvNombreArtista.text = album.artistName ?: "Artista desconocidos"

            Glide.with(this)
                .load(album.obtenerImagenGrande())
                .centerCrop()
                .into(imgPortada)

            // Mostrar toda la informacion
            contenedor.addView(vistaAlbum)

        }
    }
}