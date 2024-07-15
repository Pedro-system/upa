package com.test.test.data.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.test.test.domain.model.PokemonWithFrontImage
import com.test.test.domain.model.PokemonWithFrontImageCategory

@Dao
interface CatalogDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemons: List<PokemonWithFrontImage>)

    @Query("""
        SELECT 
        catalog.pokemonId,name, IFNULL(front_default,"") AS url,favorite  
        FROM catalog LEFT  JOIN  sprite ON catalog.pokemonId= sprite.pokemonId 
        ORDER BY catalog.pokemonId ASC
    """)
    fun getAllPokemons() : LiveData<List<PokemonWithFrontImage>>

    @Update( entity = PokemonWithFrontImage::class )
    suspend fun updatePokemon(obj:PokemonWithFrontImageCategory) : Int
}
