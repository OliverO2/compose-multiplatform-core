/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.compose.mpp.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Application


val iosCutoutInset = object : WindowInsets {
    override fun getTop(density: Density): Int = 400 //todo
    override fun getLeft(density: Density, layoutDirection: LayoutDirection): Int = 0
    override fun getRight(density: Density, layoutDirection: LayoutDirection): Int = 0
    override fun getBottom(density: Density): Int = 0
}

fun getViewControllerWithCompose() =
    Application {
        val t = remember { mutableStateOf("text state") }
        Box(Modifier.windowInsetsPadding(iosCutoutInset)) {
            Box(Modifier.fillMaxSize().windowInsetsPadding(iosCutoutInset).background(Color.Magenta)) {
                Text(buildString { repeat(10){appendLine("Up ${it + 1}")} }, Modifier.align(Alignment.TopStart).background(Color.LightGray))
                TextField(t.value, {t.value = it}, Modifier.align(Alignment.Center).fillMaxWidth())
                Text(buildString { repeat(10){appendLine("Down ${it+ 1}")} }, Modifier.align(Alignment.BottomStart).background(Color.LightGray))
            }
        }
    }
