import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.runtime.Composable

@Composable
fun test() {
    val size = LocalWindowInfo.current.containerSize
}
