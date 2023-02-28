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

import androidx.compose.runtime.snapshots.SnapshotStateObserver

internal class Updater<T : Any>(
    private val component: T,
    update: (T) -> Unit
) {
    private var isDisposed = false
    private val isUpdateScheduled = createInternalBooleanAtomic(false)
    private val snapshotObserver = SnapshotStateObserver { command ->
        command()
    }

    private val scheduleUpdate = { _: T ->
        if (!isUpdateScheduled.getAndSet(true)) {
            safelyModifyUI {
                isUpdateScheduled.set(false)
                if (!isDisposed) {
                    performUpdate()
                }
            }
        }
    }

    var update: (T) -> Unit = update
        set(value) {
            if (field != value) {
                field = value
                performUpdate()
            }
        }

    private fun performUpdate() {
        // don't replace scheduleUpdate by lambda reference,
        // scheduleUpdate should always be the same instance
        snapshotObserver.observeReads(component, scheduleUpdate) {
            update(component)
        }
    }

    init {
        snapshotObserver.start()
        performUpdate()
    }

    fun dispose() {
        snapshotObserver.stop()
        snapshotObserver.clear()
        isDisposed = true
    }
}

/**
 * safely run action, what can modify UI.
 * @param action An action that will modify UI.
 */
internal expect inline fun safelyModifyUI(crossinline action: () -> Unit)

internal expect class InternalAtomicBoolean

/**
 * Atomically sets the value to {@code newValue} and returns the old value,
 * @param newValue the new value
 * @return the previous value
 */
internal expect fun InternalAtomicBoolean.getAndSet(newValue: Boolean): Boolean

/**
 * Sets the value to {@code newValue},
 * @param newValue the new value
 */
internal expect fun InternalAtomicBoolean.set(newValue: Boolean)

internal expect fun createInternalBooleanAtomic(initialValue: Boolean): InternalAtomicBoolean
