package ru.hse.spb.sharkova.printfile

import org.gradle.api.Plugin
import org.gradle.api.Project

class PrintFilePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.run {
            create("printfile", PrintFileExtension::class.java)
        }

        target.afterEvaluate {
            if (!extension.enabled) return@afterEvaluate
            if (extension.filename.isNotEmpty()) {
                println("${extension.filename} contents with lines skipped:")
            }
        }

        with(target.tasks) {
            create("printfile", PrintFileTask::class.java) {
                it.group = "Development"
                it.description = "Output file content with specified lines skipped"
            }
        }
    }
}