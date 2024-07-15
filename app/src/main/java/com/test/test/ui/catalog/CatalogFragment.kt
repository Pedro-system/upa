package com.test.test.ui.catalog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.test.test.R
import com.test.test.data.repository.PokemonDatabase
import com.test.test.domain.model.CatalogRepository
import com.test.test.presenter.model.PokemonPresentationModel
import com.test.test.ui.theme.UpaxTheme
import com.test.test.ui.list.slides

class CatalogFragment : Fragment()
{
    private val vm: CatalogViewModel
            by lazy {
                val db = PokemonDatabase.getInstance(requireContext())
                ViewModelProvider(this,
                                  CatalogViewModelFactory(CatalogRepository(db))
                                  )[CatalogViewModel::class.java]
            }
    private val navController: NavController
        get()
        {
            return NavHostFragment.findNavController(this)
        }

    private lateinit var composeView : ComposeView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).also { composeView = it }

    private fun setSlideContent(
        slides: List<PokemonPresentationModel>,
    )
    {
        composeView.setContent {
            UpaxTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    slides(
                        slides = slides,
                        density = requireContext().resources.displayMetrics.density,
                        placeholder = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.baseline_person
                        )!!,
                        onclick = vm::onFavPokemon
                        , showDetail = {
                            Bundle().apply {
                                putLong("pokemonId",it.pokemonId)
                                putBoolean("fav",it.favorite)
                            }.let {
                                navController.navigate(R.id.action_navigation_catalog_to_navigation_detail,it)
                            }
                        }
                    ) {  vm.onFetchPokemons(slides.size) }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        vm.catalog.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            setSlideContent(it)
        })

        vm.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            // Handle error message display or logging
            Log.e("Pokemon", "Error: $errorMessage")
        })
    }

}