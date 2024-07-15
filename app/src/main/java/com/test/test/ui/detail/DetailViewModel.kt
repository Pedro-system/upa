package com.test.test.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.test.test.data.repository.pokemonService
import com.test.test.domain.model.PokemonDetailModel
import com.test.test.domain.model.PokemonMapper
import com.test.test.domain.model.PokemonRepository
import com.test.test.domain.model.PokemonTypeCrossRef
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class DetailViewModelFactory(private val pokemonRepository: PokemonRepository, val id : Long) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if(modelClass.isAssignableFrom(DetailViewModel::class.java))
        {
            return DetailViewModel(pokemonRepository,id) as T
        }
        else
        {
            throw IllegalArgumentException()
        }
    }
}


class DetailViewModel(
    private val pokemonRepository: PokemonRepository
    , val id:Long
) : ViewModel()
{

    var pokemon = pokemonRepository.getPokemonById(id)

    private val pokemonTypesColors = hashMapOf(
        "normal" to "#a8a878",
        "fire" to "#f08030",
        "water" to "#6890f0",
        "electric" to "#f8d030",
        "grass" to "#78c850",
        "ice" to "#98d8d8",
        "fighting" to "#c03028",
        "poison" to "#a040a0",
        "ground" to "#e0c068",
        "psychic" to "#f85888"
        ,"bug" to "#a8b820"
    )

    fun getTypeColor(type:String) = pokemonTypesColors[type] ?: "#ffffff"

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun onFetchDetail()
    {
        viewModelScope.launch {
            try
            {
                val pokemon = pokemonService.getPokemonById(id)
                populatePokemonDetail(pokemon)
            } catch (e: Exception)
            {
                handleNetworkError(e)
            }
        }
    }

    private suspend fun populatePokemonDetail(pokemon: PokemonDetailModel)
    {
        val mapper = PokemonMapper(pokemon)
        pokemonRepository.insertSprite(mapper.getSpritesModel())
        val typeId = pokemonRepository.insertType(mapper.getType())
        pokemonRepository.insertPokemonTypeCrossRef(PokemonTypeCrossRef(pokemon.id, typeId))
        pokemonRepository.insertPokemon(mapper.getPokemon())
    }

    private fun handleNetworkError(e: Exception)
    {
        when (e)
        {
            is IOException   -> _error.value = "Network error occurred"
            is HttpException -> _error.value = "HTTP error ${e.code()}"
            else             -> _error.value = "Unknown error"
        }
    }

    fun clearError()
    {
        _error.value = null
    }
}