package com.diegoferreiracaetano.dlearn.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.ui.components.alert.SnackbarType
import com.diegoferreiracaetano.dlearn.ui.components.alert.showAppSnackBar
import com.diegoferreiracaetano.dlearn.ui.components.button.AppButton
import com.diegoferreiracaetano.dlearn.ui.components.button.AppSelectionSimple
import com.diegoferreiracaetano.dlearn.ui.components.button.AppSwitcher
import com.diegoferreiracaetano.dlearn.ui.components.button.ButtonType
import com.diegoferreiracaetano.dlearn.ui.components.carousel.BannerCarousel
import com.diegoferreiracaetano.dlearn.ui.components.carousel.BannerItem
import com.diegoferreiracaetano.dlearn.ui.components.carousel.Carousel
import com.diegoferreiracaetano.dlearn.ui.components.carousel.CarouselItem
import com.diegoferreiracaetano.dlearn.ui.components.carousel.FullScreenBanner
import com.diegoferreiracaetano.dlearn.ui.components.carousel.MovieCarouselItemCard
import com.diegoferreiracaetano.dlearn.ui.components.image.AppImage
import com.diegoferreiracaetano.dlearn.ui.components.image.CircularImage
import com.diegoferreiracaetano.dlearn.ui.components.loading.AppLoading
import com.diegoferreiracaetano.dlearn.ui.components.navigation.AppBottomNavigation
import com.diegoferreiracaetano.dlearn.ui.components.navigation.AppContainer
import com.diegoferreiracaetano.dlearn.ui.components.navigation.AppTopBar
import com.diegoferreiracaetano.dlearn.ui.components.navigation.tabList
import com.diegoferreiracaetano.dlearn.ui.components.textfield.AppOtpVerification
import com.diegoferreiracaetano.dlearn.ui.components.textfield.AppTextField
import com.diegoferreiracaetano.dlearn.ui.components.textfield.TextFieldType
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.banner1
import dlearn.composeapp.generated.resources.banner2
import dlearn.composeapp.generated.resources.banner3
import dlearn.composeapp.generated.resources.cup
import dlearn.composeapp.generated.resources.dlearn_logo
import dlearn.composeapp.generated.resources.email_message_validation
import dlearn.composeapp.generated.resources.password_message_validation
import dlearn.composeapp.generated.resources.title_email
import dlearn.composeapp.generated.resources.title_password
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentGallery() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var switchChecked by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf("") }
    var otpValue by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(tabList.first().route) }

    AppContainer(
        snackBarHostState = snackbarHostState,
        topBar = AppTopBar(title = "Component Gallery"),
        bottomBar = AppBottomNavigation(
            items = tabList,
            selectedRoute = selectedTab,
            onTabSelected = { selectedTab = it })
    ) { modifier ->
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val dummyBanners = listOf(
                BannerItem(
                    id = "1",
                    title = "Black Panther: Wakanda Forever",
                    subtitle = "On March 2, 2022",
                    imageResource = Res.drawable.banner1
                ),
                BannerItem(
                    "2",
                    "Dune: Part Two",
                    "Epic sci-fi adventure",
                    imageResource = Res.drawable.banner2
                ),
                BannerItem(
                    "3",
                    "Spider-Man: No Way Home",
                    "The multiverse shattered",
                    imageResource = Res.drawable.banner3
                )
            )

            FullScreenBanner(
                banners = dummyBanners,
                onItemClick = { item -> println("Clicked ${item.title}") },
            )
            Column(
                modifier = Modifier.padding( 16.dp)
            ) {

                BannerCarousel(
                    banners = dummyBanners,
                    onItemClick = { item -> println("Clicked ${item.title}") } // Ação de clique simulada
                )

                val items = List(5) { index ->
                    CarouselItem(
                        id = index,
                        imageRes = Res.drawable.cup,
                        title = "Movie Title Long Name $index",
                        subtitle = "Action, Adventure",
                        rating = 4.5f,
                    )
                }

                Carousel(
                    title = "New Releases",
                    items = items,
                    onItemClick = {},
                    modifier = Modifier.padding(top = 16.dp),
                ) { item ->
                    MovieCarouselItemCard(item = item)
                }

                Button(onClick = {
                    scope.launch {
                        snackbarHostState.showAppSnackBar(
                            this,
                            "This is a success message",
                            type = SnackbarType.SUCCESS
                        )
                    }
                }) {
                    Text("Show Success Snackbar")
                }
                Button(onClick = {
                    scope.launch {
                        snackbarHostState.showAppSnackBar(
                            this,
                            "This is an error message",
                            type = SnackbarType.ERROR
                        )
                    }
                }) {
                    Text("Show Error Snackbar")
                }
                Button(onClick = {
                    scope.launch {
                        snackbarHostState.showAppSnackBar(
                            this,
                            "This is a warning message",
                            type = SnackbarType.WARNING
                        )
                    }
                }) {
                    Text("Show Warning Snackbar")
                }

                AppImage(imageURL = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png")

                CircularImage(resource = Res.drawable.dlearn_logo)

                AppButton(text = "Primary", onClick = {})
                AppButton(text = "Secondary", onClick = {}, type = ButtonType.SECONDARY)
                AppButton(text = "Tertiary", onClick = {}, type = ButtonType.TERTIARY)

                AppSwitcher(isChecked = switchChecked, onCheckedChange = { switchChecked = it })

                AppSelectionSimple(
                    list = listOf("Option 1", "Option 2", "Option 3"),
                    selected = { _, _ -> })

                AppLoading()

                AppTextField(
                    value = textFieldValue,
                    label = Res.string.title_email,
                    onValueChange = { textFieldValue = it },
                    placeholder = Res.string.title_email,
                    isError = textFieldValue.contains("cm"),
                    type = TextFieldType.EMAIL,
                    supportingText = Res.string.email_message_validation,
                )

                AppTextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    placeholder = Res.string.title_password,
                    isError = textFieldValue.length < 6,
                    type = TextFieldType.PASSWORD,
                    supportingText = Res.string.password_message_validation,
                )

                AppOtpVerification(
                    otpText = otpValue,
                    onOtpTextChange = { text, _ -> otpValue = text },
                    onResendClick = {}
                )
            }
        }
    }
}

@Preview
@Composable
fun ComponentGalleryPreview() {
    DLearnTheme {
        ComponentGallery()
    }
}
