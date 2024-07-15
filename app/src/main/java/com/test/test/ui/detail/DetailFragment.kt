package com.test.test.ui.detail

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.test.test.R
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.test.test.data.repository.PokemonDatabase
import com.test.test.databinding.FragmentDetailBinding
import com.test.test.domain.model.PokemonRepository
import com.test.test.domain.model.PokemonWithMetadataModel

class DetailFragment : Fragment()
{
    private var _binding: FragmentDetailBinding? = null

    private val vm by lazy {
        val pokemonId = DetailFragmentArgs.fromBundle(requireArguments()).pokemonId
        val fac = DetailViewModelFactory(PokemonRepository(PokemonDatabase.getInstance(requireContext())),pokemonId)
        ViewModelProvider(this,fac)[DetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDetailBinding.inflate(inflater,container,false).also { _binding=it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        vm.pokemon.observe(viewLifecycleOwner, Observer {
            if(it == null)
            {
                vm.onFetchDetail()
            }
            else
            {
                showPokemon(it)
                Log.i("Detail",it.toString())
            }
        })
        vm.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            // Handle error message display or logging
            errorMessage?:return@Observer
            MaterialAlertDialogBuilder(requireContext())
                .setCancelable(false)
                .setMessage(getString(R.string.error,errorMessage))
                .setTitle(R.string.alert)
                .setPositiveButton(com.test.mylocations.R.string.ok, {_,_->
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                ).show()
            vm.clearError()
            Log.e("Pokemon", "Error: $errorMessage")
        })
    }


    private fun showPokemon(pokemon: PokemonWithMetadataModel)
    {
        _binding?.textName?.text = pokemon.pokemonModel.name
        "${pokemon.pokemonModel.height} mts.".let {
            _binding?.textHeight?.text = it
        }
        "${pokemon.pokemonModel.weight} kgs.".let {
            _binding?.textWeight?.text = it
        }
        with(Glide.with(_binding!!.root))
        {
            this.load( pokemon.sprites.front_default ).into(_binding!!.profileImage)
            this.load( pokemon.sprites.back_default ).into(_binding!!.imageView1)
            this.load( pokemon.sprites.front_shiny ).into(_binding!!.imageView2)
            this.load( pokemon.sprites.front_default ).into(_binding!!.imageView3)
        }
        val fav = DetailFragmentArgs.fromBundle(requireArguments()).fav
        _binding?.fav?.setImageDrawable( ContextCompat.getDrawable(requireContext(),
            if(fav) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
        ) )

        pokemon.types.getOrNull(0)?.name?.lowercase()?.let {
           _binding?.card?.backgroundTintList = ColorStateList.valueOf(
               Color.parseColor(vm.getTypeColor(it))
           )
        }
    }
}