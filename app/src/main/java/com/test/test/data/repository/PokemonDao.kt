package com.test.test.data.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.test.domain.model.PokemonModel
import com.test.test.domain.model.PokemonTypeCrossRef
import com.test.test.domain.model.PokemonWithMetadataModel
import com.test.test.domain.model.SpritesModel
import com.test.test.domain.model.TypeXModel

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonModel) : Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertType(type: TypeXModel):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSprite(sprites: SpritesModel):Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPokemonTypeCrossRef(pokemonTypeCrossRef: PokemonTypeCrossRef):Long

    @Query("SELECT * FROM pokemon WHERE pokemonId = :id")
    fun getPokemonById(id: Long): LiveData<PokemonWithMetadataModel>
}
