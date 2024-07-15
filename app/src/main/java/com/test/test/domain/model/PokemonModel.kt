package com.test.test.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "pokemon")
data class PokemonModel(
    @PrimaryKey(autoGenerate = false) val pokemonId: Long,
    val name: String,
    val height: Int,
    val weight: Int
)

data class PokemonWithMetadataModel(
    @Embedded val pokemonModel: PokemonModel,
    @Relation(parentColumn = "pokemonId", entityColumn = "pokemonId")
    val sprites: SpritesModel,
    @Relation(parentColumn = "pokemonId", entityColumn = "typeId" , associateBy = Junction( PokemonTypeCrossRef::class))
    val types: List<TypeXModel>
)

@Entity(primaryKeys = ["pokemonId", "typeId"])
data class PokemonTypeCrossRef(
    val pokemonId: Long,
    val typeId: Long
)

@Entity(tableName = "sprite")
data class SpritesModel(
    @PrimaryKey(autoGenerate = false) val pokemonId: Long,
    val back_default: String,
    val back_shiny: String,
    val front_default: String,
    val front_shiny: String,
)

@Entity(tableName = "type")
data class TypeXModel(
    @PrimaryKey(autoGenerate = true) val typeId:Long = 0,
    val name: String,
    val url: String
)

@Entity(tableName = "catalog")
data class PokemonWithFrontImage(
    @PrimaryKey var pokemonId: Long = 0,
    val name: String
    ,var url: String
    ,val favorite  : Boolean
)

data class PokemonWithFrontImageCategory(
    var pokemonId: Long
    ,val favorite  : Boolean
)

data class PokemonListModel(
    val next: String = "", val previous: String = "", val results: List<PokemonListItemModel>
)

data class PokemonListItemModel(
    val name: String,
    var url: String
)
