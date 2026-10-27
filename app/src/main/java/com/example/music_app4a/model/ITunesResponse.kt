package com.example.music_app4a.model

import android.app.appsearch.SearchResults

// Archivo para vefiricar si obtenemos una respuesta del Api

data class ITunesResponse(

    val resultCount: Int,
    val results: List<Album>

)
