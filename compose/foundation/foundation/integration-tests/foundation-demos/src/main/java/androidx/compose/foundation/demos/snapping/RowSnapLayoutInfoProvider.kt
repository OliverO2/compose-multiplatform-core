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

package androidx.compose.foundation.demos.snapping

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.ui.unit.Density
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sign

@OptIn(ExperimentalFoundationApi::class)
fun SnapLayoutInfoProvider(
    scrollState: ScrollState,
    itemSize: Density.() -> Float,
    layoutSize: Density.() -> Float
) = object : SnapLayoutInfoProvider {

    fun Density.nextFullItemCenter(layoutCenter: Float): Float {
        val intItemSize = itemSize().roundToInt()
        return floor((layoutCenter + calculateSnapStepSize()) / itemSize().roundToInt()) *
            intItemSize
    }

    fun Density.previousFullItemCenter(layoutCenter: Float): Float {
        val intItemSize = itemSize().roundToInt()
        return ceil((layoutCenter - calculateSnapStepSize()) / itemSize().roundToInt()) *
            intItemSize
    }

    override fun Density.calculateSnappingOffset(currentVelocity: Float): Float {
        val layoutCenter = layoutSize() / 2f + scrollState.value + calculateSnapStepSize() / 2f
        val lowerBound = nextFullItemCenter(layoutCenter) - layoutCenter
        val upperBound = previousFullItemCenter(layoutCenter) - layoutCenter

        return calculateFinalOffset(currentVelocity, upperBound, lowerBound)
    }

    override fun Density.calculateSnapStepSize(): Float {
        return itemSize()
    }

    override fun Density.calculateApproachOffset(initialVelocity: Float): Float = 0f
}

internal fun calculateFinalOffset(velocity: Float, lowerBound: Float, upperBound: Float): Float {

    fun Float.isValidDistance(): Boolean {
        return this != Float.POSITIVE_INFINITY && this != Float.NEGATIVE_INFINITY
    }

    val finalDistance = when (sign(velocity)) {
        0f -> {
            if (abs(upperBound) <= abs(lowerBound)) {
                upperBound
            } else {
                lowerBound
            }
        }

        1f -> upperBound
        -1f -> lowerBound
        else -> 0f
    }

    return if (finalDistance.isValidDistance()) {
        finalDistance
    } else {
        0f
    }
}