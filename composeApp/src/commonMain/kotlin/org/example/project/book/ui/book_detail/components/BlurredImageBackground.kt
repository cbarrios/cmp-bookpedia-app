package org.example.project.book.ui.book_detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cmp_bookpedia_app.composeapp.generated.resources.Res
import cmp_bookpedia_app.composeapp.generated.resources.arrow_back_24px
import cmp_bookpedia_app.composeapp.generated.resources.book_cover
import cmp_bookpedia_app.composeapp.generated.resources.book_error_2
import cmp_bookpedia_app.composeapp.generated.resources.favorite_24px
import cmp_bookpedia_app.composeapp.generated.resources.favorite_nofilled_24px
import cmp_bookpedia_app.composeapp.generated.resources.go_back
import cmp_bookpedia_app.composeapp.generated.resources.mark_as_favorite
import cmp_bookpedia_app.composeapp.generated.resources.remove_from_favorites
import coil3.compose.rememberAsyncImagePainter
import org.example.project.core.ui.DarkBlue
import org.example.project.core.ui.DesertWhite
import org.example.project.core.ui.PulseAnimation
import org.example.project.core.ui.SandYellow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun BlurredImageBackground(
    imageUrl: String?,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var imageLoadResult by remember {
        mutableStateOf<Result<Painter>?>(null)
    }
    val painter = rememberAsyncImagePainter(
        model = imageUrl,
        onSuccess = {
            val size = it.painter.intrinsicSize
            imageLoadResult = if (size.width > 1 && size.height > 1) {
                Result.success(it.painter)
            } else {
                Result.failure(Exception("Invalid image dimensions"))
            }
        },
        onError = {
            it.result.throwable.printStackTrace()
        }
    )

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxWidth()
                    .background(DarkBlue)
            ) {
                Image(
                    painter = painter,
                    contentDescription = stringResource(Res.string.book_cover),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(20.dp)
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxWidth()
                    .background(DesertWhite)
            )
        }
        IconButton(
            onClick = onBackClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = DesertWhite
            ),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    top = 16.dp,
                    start = 16.dp
                )
                .statusBarsPadding()
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.arrow_back_24px),
                contentDescription = stringResource(Res.string.go_back),
                tint = Color.Black
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.15f))
            ElevatedCard(
                modifier = Modifier
                    .height(230.dp)
                    .aspectRatio(2 / 3f),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 15.dp
                )
            ) {
                AnimatedContent(
                    targetState = imageLoadResult
                ) { result ->
                    when (result) {
                        null -> Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            PulseAnimation(
                                modifier = Modifier.size(60.dp)
                            )
                        }

                        else -> {
                            Box {
                                Image(
                                    painter = if (result.isSuccess) painter else painterResource(Res.drawable.book_error_2),
                                    contentDescription = stringResource(Res.string.book_cover),
                                    modifier = Modifier.fillMaxSize().background(Color.Transparent),
                                    contentScale = if (result.isSuccess) ContentScale.Crop else ContentScale.Fit
                                )
                                IconButton(
                                    onClick = onFavoriteClick,
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(SandYellow, Color.Transparent),
                                                radius = 70f
                                            )
                                        )
                                ) {
                                    val resource =
                                        if (isFavorite) Res.drawable.favorite_24px else Res.drawable.favorite_nofilled_24px
                                    val descResource =
                                        if (isFavorite) Res.string.remove_from_favorites else Res.string.mark_as_favorite
                                    Icon(
                                        imageVector = vectorResource(resource),
                                        tint = Color.Red,
                                        contentDescription = stringResource(descResource)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            content()
        }
    }
}