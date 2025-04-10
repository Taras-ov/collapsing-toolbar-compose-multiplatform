package collapsing.toolbar.cmp

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun App(
) {
    MaterialTheme {
        CollapsingToolbarWithSnap()
    }
}

operator fun Dp.minus(value: Float): Dp = (this.value - value).dp
operator fun Dp.plus(value: Float): Dp = (this.value + value).dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingToolbarWithSnap() {
    val maxHeight = 60.dp
    val minHeight = 0.dp
    val snapThreshold = 30f

    var toolbarHeightPx by remember { mutableStateOf(maxHeight.value) }
    val animatedHeight by animateDpAsState(
        targetValue = toolbarHeightPx.dp.coerceIn(minHeight, maxHeight),
        label = "AnimatedToolbarHeight"
    )

    var scrollJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if (delta != 0f) {
                    toolbarHeightPx = (toolbarHeightPx + delta / 2)
                        .coerceIn(minHeight.value, maxHeight.value)

                    // Перезапускаем задержку для snap-а
                    scrollJob?.cancel()
                    scrollJob = coroutineScope.launch {
                        delay(200L) // Ждём окончания скролла
                        toolbarHeightPx = if (toolbarHeightPx < snapThreshold) {
                            minHeight.value
                        } else {
                            maxHeight.value
                        }
                    }
                }
                return Offset.Zero
            }
        }
    }

    val items = remember { List(50) { "Item #$it" } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue)
            .nestedScroll(nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        TopAppBar(
            title = { Text("Collapsing Toolbar") },
            modifier = Modifier.height(animatedHeight),
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Blue),
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.LightGray),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            items(items) {
                Text(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

