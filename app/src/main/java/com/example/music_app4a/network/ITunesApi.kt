package com.example.music_app4a.network

import com.example.music_app4a.model.ITunesResponse
import retrofit2.http.GET
import retrofit2.http.Query

// Archivo que permite generar la peticion al Api

interface ITunesApi {

    @GET("search")

    // Metodo para buscar el album por genero
    fun obtenerAlbumGenero(
        @Query("term") genero : String,
        @Query("country") pais : String,
        @Query("media") media : String,
        @Query("entity") entidad : String,
        @Query("limit") limite: Int,
    ) : retrofit2.Call<ITunesResponse>


}