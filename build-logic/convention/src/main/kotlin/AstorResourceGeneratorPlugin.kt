import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class AstorResourceGeneratorPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val generateTask = tasks.register("generateAstorResourceList") {
                val outputDir = layout.buildDirectory.dir("generated/kotlin")
                val outputFile = outputDir.map {
                    it.file("io/lb/astormemory/resources/AstorResources.kt")
                }
                val drawableDir = file("src/commonMain/composeResources/drawable")

                inputs.dir(drawableDir)
                outputs.file(outputFile)

                doLast {
                    val astorFiles = drawableDir.listFiles()
                        ?.filter { it.extension in listOf("png", "jpg", "jpeg", "webp") }
                        ?.sortedBy { it.name }
                        ?: emptyList()

                    val resourceNames = astorFiles.mapIndexed { index, file ->
                        val resourceName = file.nameWithoutExtension
                        val name = resourceName
                            .replace(Regex("_?\\d+$"), "")
                            .replace("_", " ")
                            .split(" ")
                            .joinToString(" ") { word ->
                                word.replaceFirstChar { c -> c.uppercase() }
                            }
                        """
                            AstorResourceInfo(
                                id = ${index + 1},
                                name = "$name",
                                resourceName = "$resourceName"
                            )
                        """.trimIndent()
                    }

                    val fileContent = """
                        package io.lb.astormemory.resources
                        
                        data class AstorResourceInfo(
                            val id: Int,
                            val name: String,
                            val resourceName: String
                        )
                        
                        object AstorResources {
                            val all = listOf(
                                ${resourceNames.joinToString(",\n                            ")}
                            )
                        }
                    """.trimIndent()

                    outputFile.get().asFile.apply {
                        parentFile.mkdirs()
                        writeText(fileContent)
                    }
                }
            }

            tasks.matching { it.name.startsWith("compileKotlin") }.configureEach {
                dependsOn(generateTask)
            }

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.named("commonMain") {
                    kotlin.srcDir(layout.buildDirectory.dir("generated/kotlin"))
                }
            }
        }
    }
}
