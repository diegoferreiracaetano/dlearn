package com.diegoferreiracaetano.dlearn.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.PageCarousel
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImage
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource.Resource
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.onboarding1
import dlearn.composeapp.generated.resources.onboarding2
import dlearn.composeapp.generated.resources.onboarding3
import dlearn.composeapp.generated.resources.onboarding_description_1
import dlearn.composeapp.generated.resources.onboarding_description_2
import dlearn.composeapp.generated.resources.onboarding_description_3
import dlearn.composeapp.generated.resources.onboarding_title_1
import dlearn.composeapp.generated.resources.onboarding_title_2
import dlearn.composeapp.generated.resources.onboarding_title_3
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private data class OnboardingPageModel(
    val title: StringResource,
    val description: StringResource,
    val image: DrawableResource,
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pages =
        listOf(
            OnboardingPageModel(
                Res.string.onboarding_title_1,
                Res.string.onboarding_description_1,
                Res.drawable.onboarding1,
            ),
            OnboardingPageModel(
                Res.string.onboarding_title_2,
                Res.string.onboarding_description_2,
                Res.drawable.onboarding2,
            ),
            OnboardingPageModel(
                Res.string.onboarding_title_3,
                Res.string.onboarding_description_3,
                Res.drawable.onboarding3,
            ),
        )

    PageCarousel(
        pageCount = pages.size,
        onFinish = onFinish,
        modifier = modifier,
        imageContent = { pageIndex ->
            if (pageIndex == 0) {
                AppImage(
                    source = Resource(pages[pageIndex].image),
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    AppImage(
                        source = Resource(pages[pageIndex].image),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .weight(2.5f),
                    )
                    Spacer(modifier = Modifier.weight(2f))
                }
            }
        },
        infoContent = { pageIndex ->
            val currentPage = pages[pageIndex]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                Text(
                    text = stringResource(currentPage.title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(currentPage.description),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    )
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    DLearnTheme {
        OnboardingScreen(onFinish = {})
    }
}
