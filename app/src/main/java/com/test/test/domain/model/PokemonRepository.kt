package com.test.test.domain.model

import androidx.lifecycle.LiveData
import com.test.test.data.repository.PokemonDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepository(private val db: PokemonDatabase)
{
    fun getPokemonById(id: Long): LiveData<PokemonWithMetadataModel> =
        db.pokemonDao().getPokemonById(id)

    suspend fun insertPokemon(pokemon: PokemonModel): Long = withContext(Dispatchers.IO) {
        return@withContext db.pokemonDao().insertPokemon(pokemon)
    }

    suspend fun insertType(type: TypeXModel): Long = withContext(Dispatchers.IO) {
        return@withContext db.pokemonDao().insertType(type)
    }

    suspend fun insertSprite(sprites: SpritesModel): Long = withContext(Dispatchers.IO) {
        return@withContext db.pokemonDao().insertSprite(sprites)
    }

    suspend fun insertPokemonTypeCrossRef(pokemonTypeCrossRef: PokemonTypeCrossRef): Long = withContext(Dispatchers.IO) {
            return@withContext db.pokemonDao().insertPokemonTypeCrossRef(pokemonTypeCrossRef)
        }
}