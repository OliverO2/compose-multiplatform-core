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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Application

val DEFAULT_COLOR = Color.Blue.copy(red = 0.7f, green = 0.7f).toArgb().toLong()
val colors: List<Long> = listOf(DEFAULT_COLOR, 0xff330080, 0xff004d4d, 0xffb3ffb3, 0xffffcccc)

data class State(
    val swiftUIDarkTheme: Boolean = false,
    val swiftUIColor: Int = DEFAULT_COLOR.toInt(),
    val composeDarkTheme: Boolean = false,
    val composeColor: Int = DEFAULT_COLOR.toInt(),
)

fun getViewControllerWithCompose(onInteract: (InteractData) -> Unit) =
    Application("Compose/Native sample") {
        val state = remember { mutableStateOf(State()) }
        fun sendInteractionData() {
            onInteract(InteractData(darkTheme = state.value.swiftUIDarkTheme, swiftUIColor = state.value.swiftUIColor))
        }
        SideEffect {
            sendInteractionData()
        }

        isSystemInDarkTheme()
        MaterialTheme(
            colors = if (state.value.composeDarkTheme) {
                darkColors(background = Color(state.value.composeColor))
            } else {
                lightColors(background = Color(state.value.composeColor))
            },
        ) {
            Surface {
                LazyColumn(Modifier.fillMaxSize().background(MaterialTheme.colors.background), horizontalAlignment = Alignment.CenterHorizontally) {
                    item {
                        Column(Modifier.padding(20.dp).border(2.dp, color = MaterialTheme.colors.primary).padding(20.dp)) {
                            Text("SwiftUI style:")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                fun changeDarkTheme() {
                                    state.value = state.value.copy(swiftUIDarkTheme = !state.value.swiftUIDarkTheme)
                                    sendInteractionData()
                                }
                                Switch(state.value.swiftUIDarkTheme, onCheckedChange = { changeDarkTheme() })
                                Text("Dark theme", Modifier.clickable { changeDarkTheme() })
                            }

                            Column {
                                Row {
                                    colors.forEach {
                                        Box(Modifier.size(40.dp).background(Color(it)).clickable {
                                            state.value = state.value.copy(swiftUIColor = it.toInt())
                                            sendInteractionData()
                                        })
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(Modifier.size(20.dp))
                    }
                    item {
                        Column(Modifier.border(2.dp, color = MaterialTheme.colors.primary).padding(20.dp)) {
                            Text("Compose style:")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                fun changeDarkTheme() {
                                    state.value = state.value.copy(composeDarkTheme = !state.value.composeDarkTheme)
                                }
                                Switch(state.value.composeDarkTheme, onCheckedChange = { changeDarkTheme() })
                                Text("Dark theme", Modifier.clickable { changeDarkTheme() })
                            }
                            Column {
                                Text("Choose Compose color")
                                Row {
                                    colors.forEach {
                                        Box(Modifier.size(40.dp).background(Color(it)).clickable {
                                            state.value = state.value.copy(composeColor = it.toInt())
                                            sendInteractionData()
                                        })
                                    }
                                }
                            }
                        }
                    }
                    items(0) {
                        val textState = remember { mutableStateOf("text field $it") }
                        TextField(value = textState.value, onValueChange = {
                            textState.value = it
                        })
                    }
                }
            }
        }

    }
