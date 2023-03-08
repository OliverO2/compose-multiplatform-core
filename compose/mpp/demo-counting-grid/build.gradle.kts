/*
 * Copyright 2021 The Android Open Source Project
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

import androidx.build.AndroidXComposePlugin
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("AndroidXPlugin")
    id("AndroidXComposePlugin")
    id("kotlin-multiplatform")
    id("application")
}

AndroidXComposePlugin.applyAndConfigureKotlinPlugin(project)

dependencies {

}

val resourcesDir = "$buildDir/resources"
val skikoWasm by configurations.creating

dependencies {
    skikoWasm(libs.skikoWasm)
}

val unzipTask = tasks.register("unzipWasm", Copy::class) {
    destinationDir = file(resourcesDir)
    from(skikoWasm.map { zipTree(it) })
}

repositories {
    mavenLocal()
}

kotlin {
    jvm("desktop")
    js(IR) {
        browser()
        binaries.executable()
    }
    wasm() {
        browser()
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
             dependencies {
                implementation(project(":compose:foundation:foundation"))
                implementation(project(":compose:foundation:foundation-layout"))
                implementation(project(":compose:material:material"))
                implementation(project(":compose:mpp"))
                implementation(project(":compose:runtime:runtime"))
                implementation(project(":compose:ui:ui"))
                implementation(project(":compose:ui:ui-graphics"))
                implementation(project(":compose:ui:ui-text"))
                implementation(libs.kotlinCoroutinesCore)
            }
        }

        val skikoMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.skikoCommon)
            }
        }

        val desktopMain by getting {
            dependsOn(skikoMain)
            dependencies {
                implementation(libs.skikoCurrentOs)
                implementation(project(":compose:desktop:desktop"))
            }
        }

        val jsMain by getting {
            dependsOn(skikoMain)
            resources.setSrcDirs(resources.srcDirs)
            resources.srcDirs(unzipTask.map { it.destinationDir })
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }

        val wasmMain by getting {
            dependsOn(skikoMain)
            resources.setSrcDirs(resources.srcDirs)
            resources.srcDirs(unzipTask.map { it.destinationDir })
            dependencies {
                implementation(kotlin("stdlib-wasm"))
            }
        }
    }
}

tasks.create("runDesktop", JavaExec::class.java) {
    dependsOn(":compose:desktop:desktop:jar")
    main = "MainKt"
    systemProperty("skiko.fps.enabled", "true")
    val compilation = kotlin.jvm("desktop").compilations["main"]
    classpath =
        compilation.output.allOutputs +
            compilation.runtimeDependencyFiles
}

project.tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += listOf(
//        "-Xklib-enable-signature-clash-checks=false",
        //"-Xplugin=${project.properties["compose.plugin.path"]}",
        "-Xir-dce",
        "-Xwasm-generate-wat",
        "-Xwasm-enable-array-range-checks"
    )
}
