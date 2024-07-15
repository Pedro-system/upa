package com.test.test.ui.list

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.test.test.presenter.model.PokemonPresentationModel


@Composable
fun slides(
    slides: List<PokemonPresentationModel>,
    density: Float, placeholder: Drawable,
    onclick: (Long,Boolean) -> Unit,
    showDetail: (PokemonPresentationModel) -> Unit,
    endListener: () -> Unit
)
{
    val state = rememberLazyListState()
    if (( state.canScrollBackward && !state.canScrollForward ) || slides.isEmpty() )
    {
        Log.i("SCROLL", "loadMore")
        endListener()
    }
    LazyColumn(
        state = state
    ) {
        items(slides) { slide ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .clickable { showDetail(slide) }
                    .padding(10.dp, Dp(0f)), contentAlignment = Alignment.Center
            ) {
                if (slide.pokemonId.toInt() == slides.size)
                {
                    CircularProgressIndicator(
                        modifier = Modifier.width(24.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                } else
                {
                    RowItem(
                        slide,
                        density,
                        placeholder,
                        onclick
                    )
                }
            }
        }
    }
}

