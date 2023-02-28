/*
 * Copyright 2023 The Android Open Source Project
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

package androidx.compose.ui.interop

import kotlinx.atomicfu.AtomicBoolean
import kotlinx.atomicfu.atomic

internal actual class InternalAtomicBoolean(val jvmAtomic: AtomicBoolean)

internal actual fun InternalAtomicBoolean.getAndSet(newValue: Boolean): Boolean =
    jvmAtomic.getAndSet(newValue)

internal actual fun InternalAtomicBoolean.set(newValue: Boolean) {
    jvmAtomic.value = newValue
}

internal actual fun createInternalBooleanAtomic(initialValue: Boolean): InternalAtomicBoolean =
    InternalAtomicBoolean(atomic(initialValue))
