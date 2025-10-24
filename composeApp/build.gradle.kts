import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "2.2.20"
}

kotlin {
    js {
        browser()
        binaries.executable()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            
            // Kotlin Serialization
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            
            // Kotlin Coroutines
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
            
            // Ktor Client
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
        
        jsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
        
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
        }
        
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

// Production build optimization
tasks.named("wasmJsBrowserDistribution") {
    dependsOn("wasmJsBrowserProductionWebpack")
}

// GitHub Pages build task
tasks.register("githubPagesBuild") {
    dependsOn("clean", "wasmJsBrowserDistribution")
    description = "Build optimized for GitHub Pages deployment"
    
    doLast {
        val webpackOutputDir = file("$buildDir/kotlin-webpack/wasmJs/productionExecutable")
        val githubPagesDir = file("$rootDir/build/github-pages")
        
        // Create GitHub Pages directory
        githubPagesDir.mkdirs()
        
        if (webpackOutputDir.exists()) {
            // Copy all webpack output files (JS, WASM, source maps, licenses)
            copy {
                from(webpackOutputDir)
                into(githubPagesDir)
                include("*.js", "*.wasm", "*.map", "*.txt")
            }
            
            // Copy HTML and CSS from wasm distribution
            val wasmDistDir = file("$rootDir/build/wasm/packages/AZ900-Quiz-composeApp/kotlin")
            if (wasmDistDir.exists()) {
                copy {
                    from(wasmDistDir)
                    into(githubPagesDir)
                    include("index.html", "styles.css")
                }
            }
            
            // Copy .nojekyll file to prevent Jekyll processing
            val nojekyllFile = file("$rootDir/.nojekyll")
            if (nojekyllFile.exists()) {
                copy {
                    from(nojekyllFile)
                    into(githubPagesDir)
                }
            }
            
            // Verify essential files exist
            val essentialFiles = listOf("composeApp.js", "index.html")
            essentialFiles.forEach { fileName ->
                val file = file("$githubPagesDir/$fileName")
                if (!file.exists()) {
                    throw GradleException("Essential file missing: $fileName")
                }
            }
            
            println("✅ GitHub Pages build completed successfully!")
            println("📁 Files copied to: ${githubPagesDir.absolutePath}")
            println("📄 Essential files verified: ${essentialFiles.joinToString(", ")}")
        } else {
            throw GradleException("❌ Webpack output directory not found: ${webpackOutputDir.absolutePath}")
        }
    }
}

