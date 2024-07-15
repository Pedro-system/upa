package com.test.test.domain.model

data class PokemonDetailModel(
    val id: Long,
    val name: String,
    val weight: Int,
    val height: Int,
    val sprites: Sprites,
    val types: List<Type>
)

data class Type(
    val slot: Int,
    val type: TypeX
)

data class Sprites(
    val pokemonId: Long,
    val back_default: String,
    val back_shiny: String,
    val front_default: String,
    val front_shiny: String,
)

data class TypeX(
    val name: String,
    val url: String
)

class PokemonMapper(val pokemon: PokemonDetailModel)
{
    fun getPokemon() = PokemonModel(
        pokemon.id
        ,pokemon.name
        ,pokemon.height
        ,pokemon.weight
    )
    fun getType() = TypeXModel(
        name = pokemon.types.getOrNull(0)?.type?.name ?: ""
        ,url =  pokemon.types.getOrNull(0)?.type?.url ?: ""
    )

    fun getSpritesModel() = SpritesModel(
        0L
        ,pokemon.id
        ,pokemon.sprites.back_default
        ,pokemon.sprites.back_shiny
        ,pokemon.sprites.front_default
        ,pokemon.sprites.front_shiny
    )
}
