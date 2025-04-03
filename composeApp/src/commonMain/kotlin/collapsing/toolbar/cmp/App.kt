package collapsing.toolbar.cmp

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {

        // For collapsing top bar
        val listState = rememberLazyListState()
        var previousOffset by remember { mutableStateOf(0) }
        var currentToolbarHeight by remember { mutableStateOf(60.dp) }

        val targetToolbarHeight by remember(currentToolbarHeight) {
            derivedStateOf {
                if (!listState.isScrollInProgress) {
                    if (currentToolbarHeight < 30.dp) 0.dp else 60.dp
                } else {
                    currentToolbarHeight
                }
            }
        }

        LaunchedEffect(listState.firstVisibleItemScrollOffset) {
            val delta = listState.firstVisibleItemScrollOffset - previousOffset
            currentToolbarHeight = (currentToolbarHeight - delta.dp).coerceIn(0.dp, 60.dp)
            previousOffset = listState.firstVisibleItemScrollOffset
        }
        val animatedToolbarHeight by animateDpAsState(
            targetValue = targetToolbarHeight,
            animationSpec = tween(300)
        )
        val toolbarContentAlpha by animateFloatAsState(
            targetValue = if (listState.isScrollInProgress) {
                currentToolbarHeight.value / 60f
            } else {
                targetToolbarHeight.value / 60f
            },
            animationSpec = tween(300)
        )
        val titleFontSize by animateFloatAsState(
            targetValue = if (listState.isScrollInProgress) {
                28f * (currentToolbarHeight.value / 60f)
            } else {
                28f * (targetToolbarHeight.value / 60f)
            },
            animationSpec = tween(300)
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
        ) {
            BaseTopBar(
                titleText = "Your title",
                height = animatedToolbarHeight,
                contentAlpha = toolbarContentAlpha,
                titleFontSize = titleFontSize
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(40) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = Color.LightGray)
                        .padding(horizontal = 16.dp),
                    ) {}
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }
    }
}

@Composable
fun BaseTopBar(
    titleText: String,
    height: Dp,
    contentAlpha: Float,
    titleFontSize: Float,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(color = Color.DarkGray)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = titleText,
            modifier = Modifier.alpha(alpha = contentAlpha),
            color = Color.White,
            fontSize = titleFontSize.sp,
        )
    }
}