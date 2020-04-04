package ru.hse.spb.sharkova.printfile

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.regex.Pattern
import kotlin.math.max

@CacheableTask
open class PrintFileTask : DefaultTask() {
    private val skipPattern = Pattern.compile("^#skip ([1-9][0-9]*)$")

    @TaskAction
    fun action() {
        val extension = project.extensions.run {
            findByName("printfile") as PrintFileExtension
        }

        if (extension.enabled) {
            if (extension.filename.isEmpty()) {
                println("No file provided.")
            } else {
                outputFileWithLinesSkipped(extension.filename)
            }
        }
    }

    private fun outputFileWithLinesSkipped(filename: String) {
        val file = File(filename)
        if (!file.isFile) {
            println("File $filename not found.")
            return
        }
        var skipCount = 0
        file.forEachLine {
            val matcher = skipPattern.matcher(it)
            if (matcher.find()) {
                val linesToSkip = matcher.group(1).toInt()
                skipCount = max(skipCount, linesToSkip)
            } else {
                if (skipCount != 0) {
                    skipCount--
                } else {
                    println(it)
                }
            }
        }
    }
}