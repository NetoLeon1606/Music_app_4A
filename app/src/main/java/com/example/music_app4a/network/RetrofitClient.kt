package com.example.music_app4a.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Archivo para construir todo el enlace del Api

object RetrofitClient {

    // Colocar la Base URL
    private const val  BASE_URL = "https://itunes.apple.com/"

    // Construir la URL completa
    val api : ITunesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

}