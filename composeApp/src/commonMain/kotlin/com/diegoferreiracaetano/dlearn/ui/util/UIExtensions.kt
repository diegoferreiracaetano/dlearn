package com.diegoferreiracaetano.dlearn.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.designsystem.components.movie.MovieItem
import com.diegoferreiracaetano.dlearn.designsystem.components.textfield.TextFieldType
import com.diegoferreiracaetano.dlearn.ui.factory.RenderComponent
import com.diegoferreiracaetano.dlearn.ui.factory.RenderComponents
import com.diegoferreiracaetano.dlearn.ui.sdui.AppErrorComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppLoadingComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTextFieldType
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieItemComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.edit_profile_title
import dlearn.composeapp.generated.resources.field_email
import dlearn.composeapp.generated.resources.field_full_name
import dlearn.composeapp.generated.resources.field_password
import dlearn.composeapp.generated.resources.field_phone_number
import dlearn.composeapp.generated.resources.home_title
import dlearn.composeapp.generated.resources.nav_favorites
import dlearn.composeapp.generated.resources.nav_home
import dlearn.composeapp.generated.resources.nav_profile
import dlearn.composeapp.generated.resources.nav_search
import dlearn.composeapp.generated.resources.nav_watchlist
import dlearn.composeapp.generated.resources.profile_title
import dlearn.composeapp.generated.resources.save_changes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

@Immutable
data class UiState<out T>(
    val success: T? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@Composable
fun Screen.Render(
    actions: ComponentActions,
    modifier: Modifier = Modifier
) {
    RenderComponents(
        components = this.components,
        actions = actions,
        modifier = modifier
    )
}

@Composable
fun UIState<Screen>.Render(
    actions: ComponentActions,
    modifier: Modifier = Modifier
) {
    when (this) {
        is UIState.Loading -> RenderComponent(
            component = AppLoadingComponent,
            actions = actions,
            modifier = modifier
        )

        is UIState.Error -> RenderComponent(
            component = AppErrorComponent(throwable),
            actions = actions,
            modifier = modifier
        )

        is UIState.Success -> {
            RenderComponents(
                components = data.components,
                actions = actions,
                modifier = modifier
            )
        }
    }
}

fun <T> Flow<T>.collectIn(
    scope: CoroutineScope,
    uiState: MutableStateFlow<UIState<T>>
) {
    scope.launch {
        this@collectIn
            .onStart { uiState.value = UIState.Loading }
            .catch { e -> uiState.value = UIState.Error(e) }
            .collect { uiState.value = UIState.Success(it) }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T, R> Flow<T?>.asUiState(
    scope: CoroutineScope,
    initialState: UiState<R> = UiState(isLoading = false),
    transform: (T) -> Flow<R>
): StateFlow<UiState<R>> {
    return this
        .filterNotNull()
        .flatMapMerge { trigger ->
            transform(trigger)
                .map { result ->
                    UiState(success = result)
                }
                .onStart { emit(UiState(isLoading = true)) }
                .catch { e ->
                    emit(UiState(error = e.message ?: "Ocorreu um erro desconhecido"))
                }
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = initialState
        )
}

fun <T, R> ViewModel.produceUiState(
    trigger: Flow<T?>,
    initialState: UiState<R> = UiState(),
    transform: (T) -> Flow<R>
): StateFlow<UiState<R>> {
    return trigger.asUiState(
        scope = this.viewModelScope,
        initialState = initialState,
        transform = transform
    )
}

fun Color.contrastTextColor(): Color {
    return if (this.luminance() > 0.5f) Color.Black else Color.White
}

fun MovieItemComponent.toMovieItem(): MovieItem {
    return MovieItem(
        id = this.id,
        title = this.title,
        imageSource = AppImageSource.Url(this.imageUrl),
        rating = this.rating?.toDoubleOrNull(),
        year = this.year.orEmpty(),
        duration = this.duration.orEmpty(),
        contentRating = this.contentRating.orEmpty(),
        genre = this.genre ?: this.subtitle.orEmpty(),
        type = this.movieType.orEmpty(),
        isPremium = this.isPremium,
        rank = this.rank
    )
}

fun AppStringType?.toResource(): StringResource? {
    return when (this) {
        AppStringType.PROFILE_TITLE -> Res.string.profile_title
        AppStringType.HOME_TITLE -> Res.string.home_title
        AppStringType.NAV_HOME -> Res.string.nav_home
        AppStringType.NAV_WATCHLIST -> Res.string.nav_watchlist
        AppStringType.NAV_SEARCH -> Res.string.nav_search
        AppStringType.NAV_FAVORITES -> Res.string.nav_favorites
        AppStringType.NAV_PROFILE -> Res.string.nav_profile
        AppStringType.EDIT_PROFILE_TITLE -> Res.string.edit_profile_title
        AppStringType.FIELD_FULL_NAME -> Res.string.field_full_name
        AppStringType.FIELD_EMAIL -> Res.string.field_email
        AppStringType.FIELD_PASSWORD -> Res.string.field_password
        AppStringType.FIELD_PHONE_NUMBER -> Res.string.field_phone_number
        AppStringType.SAVE_CHANGES -> Res.string.save_changes
        else -> null
    }
}

fun AppTextFieldType.toTextFieldType(): TextFieldType {
    return when (this) {
        AppTextFieldType.EMAIL -> TextFieldType.EMAIL
        AppTextFieldType.PASSWORD -> TextFieldType.PASSWORD
        else -> TextFieldType.NONE
    }
}
