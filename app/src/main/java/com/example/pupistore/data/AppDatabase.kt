package com.example.pupistore.data

import android.content.Context
import androidx.room.*

@Entity(tableName = "objetos")
data class Objeto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String
)

@Dao
interface ObjetoDao {
    @Insert
    suspend fun insertar(obj: Objeto)

    @Query("SELECT * FROM objetos ORDER BY id DESC")
    suspend fun obtenerTodos(): List<Objeto>
}

@Database(entities = [Objeto::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun objetoDao(): ObjetoDao
}

fun provideDB(context: Context): AppDatabase =
    Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "pupi_db"
    ).fallbackToDestructiveMigration() // opcional para dev
        .build()
