/*
 * Copyright 2020-2023 JetBrains s.r.o. and respective authors and developers.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE.txt file.
 */

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        BrowserViewportWindow("Compose/Canvas/Js Counting Grid") {
            Column {
                Text("Compose/Canvas/Js Counting Grid", style = MaterialTheme.typography.h4)
                MainScene()
            }
        }
    }
}
