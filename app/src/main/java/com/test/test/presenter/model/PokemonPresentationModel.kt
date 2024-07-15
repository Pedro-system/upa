package com.test.test.presenter.model

data class PokemonPresentationModel(
    val pokemonId : Long = 0L,
    val name :String = ""
    , val url :String = ""
    , val favorite : Boolean = false
)
