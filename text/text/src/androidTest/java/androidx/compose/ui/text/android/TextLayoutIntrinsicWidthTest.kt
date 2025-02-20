/*
 * Copyright 2018 The Android Open Source Project
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

package androidx.compose.ui.text.android

import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.text.TextPaint
import androidx.compose.ui.text.android.style.LetterSpacingSpanEm
import androidx.compose.ui.text.android.style.LetterSpacingSpanPx
import androidx.compose.ui.text.android.style.LineHeightStyleSpan
import androidx.core.content.res.ResourcesCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.testutils.fonts.R
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(InternalPlatformTextApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class TextLayoutIntrinsicWidthTest {
    private val defaultText = SpannableString("This is a callout message")

    // values are exact values for the repro case (on Pixel4, Android 11)
    private val fontScale = 1.15f
    private val density = 3.051f
    private val letterSpacingEm = 0.4f / 12f
    private val fontSize = 12f.spToPx()
    private val letterSpacingPx = 0.4f.spToPx()
    private val lineHeight = 16f.spToPx()
    private lateinit var defaultPaint: TextPaint

    @Before
    fun setup() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        defaultPaint = TextPaint().apply {
            typeface = ResourcesCompat.getFont(instrumentation.context, R.font.sample_font)!!
            textSize = fontSize
        }
    }

    @Test
    fun intrinsicWidth_with_letterSpacing_and_lineHeight_createsOneLine() {
        val text = defaultText.apply {
            setLineHeight(lineHeight)
            setSpan(LetterSpacingSpanPx(letterSpacingPx))
        }

        assertLineCount(text)
    }

    @Test
    fun intrinsicWidth_with_letterSpacing_and_lineHeight_createsOneLine_multipleSpans() {
        val text = defaultText.apply {
            for (i in 0..8) {
                val end = i + 1
                setLineHeight(lineHeight, i, end)
                setSpan(LetterSpacingSpanPx(letterSpacingPx), i, end)
            }
        }

        assertLineCount(text)
    }

    @Test
    fun intrinsicWidth_with_letterSpacingEm_and_lineHeight_createsOneLine() {
        val text = defaultText.apply {
            setLineHeight(lineHeight)
            setSpan(LetterSpacingSpanEm(letterSpacingEm))
        }

        assertLineCount(text)
    }

    @Test
    fun intrinsicWidth_with_paintLetterSpacing_and_lineHeight_createsOneLine() {
        val text = defaultText.apply {
            setLineHeight(lineHeight)
        }

        val paint = defaultPaint.apply {
            letterSpacing = letterSpacingEm
        }

        assertLineCount(text, paint)
    }

    @Test
    fun intrinsicWidth_with_letterSpacing_and_noLineHeight_createsOneLine() {
        val text = defaultText.apply {
            setSpan(LetterSpacingSpanPx(letterSpacingPx))
        }

        assertLineCount(text)
    }

    @Test
    fun intrinsicWidth_with_noLetterSpacing_and_withLineHeight_createsOneLine() {
        val text = defaultText.apply {
            setLineHeight(lineHeight)
        }

        assertLineCount(text)
    }

    @Test
    fun intrinsicWidth_with_noLetterSpacing_and_noLineHeight_createsOneLine() {
        assertLineCount(defaultText)
    }

    @Test
    fun intrinsicWidth_sameInLtrAndRtl() {
        val text = SpannableString("asdf")

        val intrinsicsLtr = LayoutIntrinsics(text, defaultPaint, LayoutCompat.TEXT_DIRECTION_LTR)
        val intrinsicsRtl = LayoutIntrinsics(text, defaultPaint, LayoutCompat.TEXT_DIRECTION_RTL)

        assertThat(intrinsicsLtr.maxIntrinsicWidth).isEqualTo(intrinsicsRtl.maxIntrinsicWidth)
    }

    @Test
    fun intrinsicWidth_sameInLtrAndRtl_withLetterSpacing() {
        val text = SpannableString("asdf").apply {
            setSpan(LetterSpacingSpanPx(letterSpacingPx))
        }

        val intrinsicsLtr = LayoutIntrinsics(text, defaultPaint, LayoutCompat.TEXT_DIRECTION_LTR)
        val intrinsicsRtl = LayoutIntrinsics(text, defaultPaint, LayoutCompat.TEXT_DIRECTION_RTL)

        assertThat(intrinsicsLtr.maxIntrinsicWidth).isEqualTo(intrinsicsRtl.maxIntrinsicWidth)
    }

    private fun assertLineCount(text: CharSequence, paint: TextPaint = defaultPaint) {
        val intrinsics = LayoutIntrinsics(text, paint, LayoutCompat.TEXT_DIRECTION_LTR)
        assertThat(
            TextLayout(
                charSequence = text,
                width = intrinsics.maxIntrinsicWidth,
                textPaint = paint
            ).lineCount
        ).isEqualTo(1)
    }

    private fun Spannable.setSpan(span: Any, start: Int = 0, end: Int = length) {
        this.setSpan(span, start, end, SPAN_INCLUSIVE_INCLUSIVE)
    }

    private fun Float.spToPx(): Float = this * fontScale * density

    private fun Spannable.setLineHeight(lineHeight: Float, start: Int = 0, end: Int = length) {
        val span = LineHeightStyleSpan(
            lineHeight = lineHeight,
            startIndex = start,
            endIndex = end,
            trimFirstLineTop = true,
            trimLastLineBottom = true,
            topRatio = -1f /* default: proportional */
        )
        setSpan(span, start, end)
    }
}