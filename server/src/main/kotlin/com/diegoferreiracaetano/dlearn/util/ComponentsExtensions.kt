package com.diegoferreiracaetano.dlearn.util

import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

fun List<Component>.toAppContainerComponent(topBar: AppTopBarComponent): Screen =
    Screen(
        components =
            listOf(
                AppContainerComponent(
                    topBar = topBar,
                    components = this,
                ),
            ),
    )
