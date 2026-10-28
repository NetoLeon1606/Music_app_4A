package com.example.music_app4a

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.music_app4a.model.ITunesResponse
import com.example.music_app4a.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class HomeActivity : AppCompatActivity() {

    private lateinit var tvResultado : TextView
    private lateinit var imgAlbum : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        tvResultado = findViewById(R.id.tvResultado)
        imgAlbum = findViewById(R.id.imgAlbum)

        obtenerAlbum()
    }

    // Metodo para cargar los albumnes
    private fun obtenerAlbum(){

        RetrofitClient.api.obtenerAlbumGenero(
            genero = "musica latina",
            pais = "MX",
            media ="music",
            entidad = "album",
            limite = 1,
        ).enqueue(object : Callback<ITunesResponse> {

            // Metodos obligatorios para el funcionanmiento
            // Metodo para verificar que la respuesta sea correcta
            override fun onResponse(call: Call<ITunesResponse?>, response: Response<ITunesResponse?>) {

                // Variable que obtenga los resultados del API
                val albumes = response.body()?.results

                if(!albumes.isNullOrEmpty()){

                    // Variable que guarde la informacion de cada respuesta obtenida del API
                    val album = albumes[0]

                    tvResultado.text = "Nombre del Album: ${album.collectionName}\n\n" +
                                        "Artista: ${album.artistName}"


                    // Mostrar imagen desde el API
                    Glide.with(this@HomeActivity)
                        .load(album.obtenerImagenGrande())
                        .into(imgAlbum)

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
}