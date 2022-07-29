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

package androidx.room.solver.shortcut.binder

import androidx.room.solver.CodeGenScope
import androidx.room.solver.shortcut.result.UpsertMethodAdapter
import androidx.room.vo.ShortcutQueryParameter
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeSpec
/**
 * Connects the upsert method, the database and the [UpsertMethodAdapter].
 */
abstract class UpsertMethodBinder(val adapter: UpsertMethodAdapter?) {

    /**
     * Received the upsert method parameters, the upsertion adapters and generations the code that
     * runs the upsert and returns the result.
     *
     * For example, for the DAO method:
     * ```
     * @Upsert
     * fun addPublishers(vararg publishers: Publisher): List<Long>
     * ```
     * The following code will be generated:
     *
     * ```
     * __db.beginTransaction();
     * try {
     *  List<Long> _result = __upsertionAdapterOfPublisher.upsertAndReturnIdsList(publishers);
     *  __db.setTransactionSuccessful();
     *  return _result;
     * } finally {
     *  __db.endTransaction();
     * }
     * ```
     */
    abstract fun convertAndReturn(
        parameters: List<ShortcutQueryParameter>,
        upsertionAdapters: Map<String, Pair<FieldSpec, TypeSpec>>,
        dbField: FieldSpec,
        scope: CodeGenScope
    )
}