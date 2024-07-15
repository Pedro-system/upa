package com.test.test.data.repository

import com.test.test.domain.model.PokemonDetailModel
import com.test.test.domain.model.PokemonListModel
import com.test.test.domain.model.PokemonModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


private val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://pokeapi.co/api/v2/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val pokemonService = retrofit.create(PokemonService::class.java)

interface PokemonService {
    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Long): PokemonDetailModel

    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") llimit : Int = 25,@Query("offset") offset: Int = 0 ): PokemonListModel
}
