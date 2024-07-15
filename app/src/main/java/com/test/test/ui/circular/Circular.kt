package com.test.test.ui.circular

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.Patterns
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.PixelSize
import coil.transform.CircleCropTransformation
import com.test.test.presenter.model.PhotoInitialsWithPlaceHolderModel


@Composable
private fun CircularImage(
    density: Float,
    url: String?,
    placeholderDrawable: Drawable?,
    errorDrawable: Drawable?,
    fallbackDrawable: Drawable?,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp
)
{
    val paint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
    }
    val painter = rememberImagePainter(
        data = url,
        builder = {
            transformations(CircleCropTransformation())
            size(
                PixelSize(
                    size.value.toInt().dpToPx(density).toInt(),
                    size.value.toInt().dpToPx(density).toInt()
                )
            )
            /**
             * Set the placeholder drawable to use when the request starts.
             */
            /**
             * Set the placeholder drawable to use when the request starts.
             */
            placeholder(placeholderDrawable)
            /**
             * Set the error drawable to use if the request fails.
             */
            /**
             * Set the error drawable to use if the request fails.
             */
            error(errorDrawable)
            /**
             * Set the fallback drawable to use if [data] is null.
             */
            /**
             * Set the fallback drawable to use if [data] is null.
             */
            fallback(fallbackDrawable)
        },
        onExecute = { _, _ -> true }, // Always execute the request
    )

    Box(modifier = modifier.size(size)) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(size)
        )

        Canvas(modifier = Modifier.size(size)) {
            drawIntoCanvas {
                it.drawCircle(
                    paint = paint.asComposePaint(),
                    center = Offset(size.toPx() / 2, size.toPx() / 2),
                    radius = size.toPx() / 2,
                )
            }
        }
    }
}

fun Int.dpToPx(density: Float): Float = (this * density)

@Composable
fun showImage(density: Float, photo: PhotoInitialsWithPlaceHolderModel)
{
    val initialsDrawable = createInitialsDrawable(
        photo.initialsModel.string, photo.initialsModel.background, photo.initialsModel.fontColor
    )
    val placeholder = photo.placeHolder
    val initialsOrPlaceHolder = if (Regex("\\p{Upper}").matches(
            photo.initialsModel.string.uppercase().firstOrNull()?.toString().orEmpty()
        )
    )
    {
        initialsDrawable
    } else
    {
        placeholder
    }
    if (Patterns.WEB_URL.matcher(photo.url).matches())
    {
        CircularImage(
            density = density,
            url = photo.url,
            placeholderDrawable = initialsOrPlaceHolder, errorDrawable = initialsOrPlaceHolder,
            fallbackDrawable = null
        )
    } else
    {
        CircularImage(
            density = density,
            url = null,
            placeholderDrawable = initialsOrPlaceHolder, errorDrawable = null,
            fallbackDrawable = initialsOrPlaceHolder
        )
    }
}

private fun createInitialsDrawable(
    name: String,
    background: Int,
    textColor: Int
) =
    name.split(" ").map { it.take(1).uppercase() }.iterator().let {
        val initials = """${
            if (it.hasNext())
            {
                it.next()
            } else ""
        }${
            if (it.hasNext())
            {
                it.next()
            } else ""
        }"""
        return@let object : Drawable()
        {
            val paint = Paint().apply {
                color = textColor
                isAntiAlias = true
                style = Paint.Style.FILL_AND_STROKE
                strokeWidth = 10f
            }
            val recF by lazy {
                RectF(bounds).apply {
                    with(paint.strokeWidth / 4f) {
                        this@apply.inset(
                            this,
                            this
                        )
                    }
                }
            }
            val path by lazy {
                android.graphics.Path().apply { addOval(recF, android.graphics.Path.Direction.CW) }
            }

            override fun draw(canvas: Canvas)
            {
                val scale = bounds.width() / (1.1f * paint.measureText("MM"))
                paint.textSize = paint.textSize.times(scale)
                val verticalOffset = paint.measureText("NN") / 2f
                val textWidth = paint.measureText(initials)
                canvas.save()
                canvas.clipPath(path)
                canvas.drawColor(background)
                paint.style = Paint.Style.FILL_AND_STROKE
                canvas.drawText(
                    initials,
                    (bounds.width() - textWidth) / 2f,
                    (bounds.height() + verticalOffset) / 2f,
                    paint
                )
                paint.style = Paint.Style.STROKE
                canvas.drawPath(path, paint)
                canvas.restore()
            }

            override fun setAlpha(alpha: Int) = Unit

            override fun setColorFilter(colorFilter: ColorFilter?) = Unit

            override fun getOpacity(): Int = PixelFormat.OPAQUE
        }
    }
