package com.test.test.ui.list

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.test.test.presenter.model.InitialsModel
import com.test.test.presenter.model.PhotoInitialsWithPlaceHolderModel
import com.test.test.presenter.model.PokemonPresentationModel
import com.test.test.ui.circular.showImage

@Composable
fun RowItem(
    slide: PokemonPresentationModel,
    density: Float,
    placeholder: Drawable,
    onClick: (Long,Boolean) -> Unit
)
{
    ElevatedCard(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
        ,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
            , horizontalArrangement = Arrangement.SpaceBetween
        ) {
                showImage(
                    density,
                    PhotoInitialsWithPlaceHolderModel(
                        slide.url, initialsModel = InitialsModel(
                            Color.parseColor("#ff0000"),
                            Color.parseColor("#00ff00"), slide.name
                        ), placeHolder = placeholder
                    )
                )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                fontSize = TextUnit(16f, TextUnitType.Sp), text = slide.name
                , modifier = Modifier.weight(1f,true)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { /* Handle icon click */ }
            ) {
                Icon(
                    imageVector = if(slide.favorite)
                    {
                        Icons.Default.Favorite
                    }else {
                        Icons.Default.FavoriteBorder
                    }
                    ,
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable {
                        onClick(slide.pokemonId, slide.favorite.not())
                    }
                )
            }
        }
    }
}
