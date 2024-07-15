package com.test.test.ui.catalog

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.test.test.data.repository.pokemonService
import com.test.test.domain.model.CatalogRepository
import com.test.test.domain.model.PokemonListModel
import com.test.test.domain.model.PokemonWithFrontImage
import com.test.test.domain.model.PokemonWithFrontImageCategory
import com.test.test.presenter.model.PokemonPresentationModel
import com.test.test.utils.toLiveData
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.IllegalArgumentException

const val  LIMIT = 25

@Suppress("UNCHECKED_CAST")
class CatalogViewModelFactory(val catalogRepository : CatalogRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if(modelClass.isAssignableFrom(CatalogViewModel::class.java))
        {
            return CatalogViewModel(catalogRepository) as T
        }
        else
        {
            throw IllegalArgumentException()
        }
    }
}

class CatalogViewModel(val catalogRepository : CatalogRepository
) : ViewModel()
{
    var catalog = catalogRepository.getAllPokemons.map { list ->
        list.map {
            PokemonPresentationModel(
                 it.pokemonId
                ,it.name
                ,it.url
                ,it.favorite
            )
        }
    }

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    fun onFetchPokemons(actualCount : Int )
    {
        Log.i("load",actualCount.toString())
        viewModelScope.launch {
            try {
                val pokemonList = pokemonService.getPokemonList(LIMIT,actualCount)
                populateCatalog(pokemonList)
            } catch (e: Exception) {
                handleNetworkError(e)
            }
        }
    }

    private suspend fun populateCatalog(pokemonList: PokemonListModel)
    {
        val list = pokemonList.results.map {
            PokemonWithFrontImage(
                Uri.parse(it.url).pathSegments.last().toLong()
                ,it.name,""
                ,false
            )
        }
        catalogRepository.insert(list)
    }

    private fun handleNetworkError(e: Exception) {
        when (e) {
            is IOException   -> _error.value = "Network error occurred"
            is HttpException -> _error.value = "HTTP error ${e.code()}"
            else             -> _error.value = "Unknown error"
        }
    }

    fun onFavPokemon(id: Long,like : Boolean)
    {
        viewModelScope.launch {
            try
            {
                catalogRepository.updatePokemon(PokemonWithFrontImageCategory(id,like) )
            } catch (e: Exception)
            {
                handleNetworkError(e)
            }
        }
    }

    private val _loader = MutableLiveData<Boolean>()
    val loader
        get() = _loader.toLiveData()

}
