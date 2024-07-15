package com.test.test.domain.model

import com.test.test.data.repository.PokemonDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CatalogRepository(private val db: PokemonDatabase)
{
    val getAllPokemons = db.catalogDao().getAllPokemons()

    suspend fun insert(pokemons: List<PokemonWithFrontImage>) =
    withContext(Dispatchers.IO) {
        return@withContext db.catalogDao().insertPokemon( pokemons)
    }

    suspend fun updatePokemon(pokemon: PokemonWithFrontImageCategory) : Int =
        withContext(Dispatchers.IO) {
            return@withContext db.catalogDao().updatePokemon( pokemon )
        }

}