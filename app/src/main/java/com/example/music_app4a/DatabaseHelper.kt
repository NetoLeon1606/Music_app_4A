package com.example.music_app4a

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION,
) {

    companion object{
        private const val DATABASE_NAME = "music.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_USUARIOS = "usuarios"
        private const val COL_ID = "id"
        private const val COL_NOMBRE = "nombre"
        private const val COL_CORREO = "correo"
        const val COL_PASSWORD = "password"

        const val TABLE_FAVORITOS = "favoritos"
        const val COL_FAVORITO = "id"
        const val COL_ALBUM_ID = "album_id"
        const val COL_NOMBRE_ALBUM = "nombre_album"
        const val COL_ARTISTA = "artista"
        const val COL_GENERO = "genero"
        const val COL_CANCIONES = "canciones"
        const val COL_FECHA = "fecha"
        const val COL_IMAGEN = "imagen"

        // Crear tabla de usuarios
        private const val CREATE_TABLE_USUARIOS = """
            CREATE TABLE $TABLE_USUARIOS(
            $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_NOMBRE TEXT NOT NULL,
            $COL_CORREO TEXT NOT NULL UNIQUE,
            $COL_PASSWORD TEXT NOT NULL
            )
        """

        // Crear tabla de favoritos
        private const val CREATE_TABLE_FAVORITOS = """
            CREATE TABLE $TABLE_FAVORITOS(
            $COL_FAVORITO INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_ALBUM_ID INTEGER UNIQUE NOT NULL,
            $COL_NOMBRE_ALBUM TEXT NOT NULL,
            $COL_ARTISTA TEXT,
            $COL_GENERO TEXT,
            $COL_CANCIONES INTEGER,
            $COL_FECHA TEXT,
            $COL_IMAGEN TEXT
            )
        """
    }



    // Metodo para crear la base de datos
    override fun onCreate(db: SQLiteDatabase?) {
        // Crear tabla de SQL
        db?.execSQL(CREATE_TABLE_USUARIOS)
        db?.execSQL(CREATE_TABLE_FAVORITOS)
    }

    // Metodo para actualizar la base de datos
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, nextVersion: Int) {
        // Vereficar que la version sea menor a 2
        if(oldVersion > 2){
            db?.execSQL(CREATE_TABLE_FAVORITOS)
        }
    }

    // Metodo para Insertar Registro
    fun registrarUsuario(nombre : String, correo : String, password : String) : Boolean{

        // Crear variable para que la base de datos se pueda modificar
        val db = writableDatabase

        // Crear variable para recibir los datos que escribe el usuario
        val datos = ContentValues().apply {
            put(COL_NOMBRE, nombre)
            put(COL_CORREO, correo)
            put(COL_PASSWORD, password)
        }

        // Insertar datos en la tabla
        val resultado = db.insert(TABLE_USUARIOS, null, datos)

        // Cerrar conexion con la BD
        db.close()

        // 1L o -1L regresa esos datos + fue exitosa - no fue exitosa
        // Comparar el valor del resultado de la insercion y regresarlo como Boolean
        return resultado != -1L
    }

    // Metodo para Consultar a un Usuario
    fun iniciarSesion(correo: String, password: String) : Boolean{

        val db = readableDatabase

        val query_sql = """
            SELECT * FROM $TABLE_USUARIOS 
            WHERE $COL_CORREO = ? AND $COL_PASSWORD = ?
        """.trimIndent()

        // Crea cursor para recorrer la informacion
        val cursor = db.rawQuery(
            query_sql, arrayOf(correo, password)
        )

        // Crear variable de tipo Bool que permita saber si el cursor encontro un registro
        val usuarioEncontrardo = cursor.count > 0

        cursor.close()

        db.close()


        return usuarioEncontrardo

    }

    // Metodo para registrar informacion proviniente del API
    fun guardarAlbumFavorito(albumId : Long, nombreAlbum : String, artista : String, genero : String, canciones : Int, fecha : String, imagen : String) : Boolean{

        // Declarar uso de la BD
        val db = writableDatabase

        val datos = ContentValues().apply {
            put(COL_ALBUM_ID, albumId)
            put(COL_NOMBRE_ALBUM, nombreAlbum)
            put(COL_ARTISTA, artista)
            put(COL_GENERO, genero)
            put(COL_CANCIONES, canciones)
            put(COL_FECHA, fecha)
            put(COL_IMAGEN, imagen)
        }

        // Insersion en la tabla
        val resultado = db.insertWithOnConflict(
            TABLE_FAVORITOS,
            null,
            datos,
            SQLiteDatabase.CONFLICT_IGNORE)

        return resultado != -1L
    }

    // Metodo para eliminar un registro
    fun eliminarAlbumFavorito(albumId: Long) : Boolean{
        val db = writableDatabase

        val filas_afectadas = db.delete(
            TABLE_FAVORITOS,
            "$COL_ALBUM_ID = ?",
            arrayOf(albumId.toString()))

        return filas_afectadas > 0
    }

    // Metodo para verificar que un registro exite
    fun existeAlbumFavorito(albumId: Long) : Boolean{
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COL_FAVORITO FROM $TABLE_FAVORITOS WHERE $COL_ALBUM_ID = ?",
            arrayOf(albumId.toString())
        )

        val existe = cursor.moveToFirst()
        cursor.close()

        return existe
    }

}