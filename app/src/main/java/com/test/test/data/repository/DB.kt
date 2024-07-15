package com.test.test.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.test.domain.model.PokemonModel
import com.test.test.domain.model.PokemonTypeCrossRef
import com.test.test.domain.model.PokemonWithFrontImage
import com.test.test.domain.model.SpritesModel
import com.test.test.domain.model.TypeXModel


@Database(entities = [PokemonModel::class
                     ,PokemonWithFrontImage::class
                     ,PokemonTypeCrossRef::class
                     ,SpritesModel::class
                     ,TypeXModel::class
                     ], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun catalogDao(): CatalogDao

    companion object {
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        fun getInstance(context: Context): PokemonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    "pokemon_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


