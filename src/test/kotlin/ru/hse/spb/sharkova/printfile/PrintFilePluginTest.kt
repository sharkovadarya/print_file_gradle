package ru.hse.spb.sharkova.printfile

import org.assertj.core.api.Assertions
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.hse.spb.sharkova.printfile.extension.TemporaryDir
import java.io.File

@ExtendWith(TemporaryDir::class)
class PrintFilePluginTest {
    private val lines = listOf(
        "What a piece of work is a man! how noble in reason!",
        "how infinite in faculty! in form and moving how",
        "express and admirable! in action how like an angel",
        "in apprehension how like a god! the beauty of the",
        "world! the paragon of animals! And yet, to me,",
        "what is this quintessence of dust? man delights not",
        "me: no, nor woman neither, though by your smiling",
        "you seem to say so."
    )

    @Test
    fun testNoLinesToSkip(tempDir: File) {
        val result = getResult("src/test/resources/noLinesToSkip.txt", tempDir)
        lines.forEach { Assertions.assertThat(result).contains(it) }
    }

    @Test
    fun skipTwoLinesInTheMiddle(tempDir: File) {
        val result = getResult("src/test/resources/skipTwoLinesInTheMiddle.txt", tempDir)
        (lines.subList(0, 4) + lines.subList(6, lines.size)).forEach { Assertions.assertThat(result).contains(it) }
        lines.subList(4, 6).forEach { Assertions.assertThat(result).doesNotContain(it) }
    }

    @Test
    fun testSkipTwoFirstLines(tempDir: File) {
        val result = getResult("src/test/resources/skipTwoFirstLines.txt", tempDir)
        lines.subList(2, lines.size).forEach { Assertions.assertThat(result).contains(it) }
        lines.subList(0, 2).forEach { Assertions.assertThat(result).doesNotContain(it) }
    }

    @Test
    fun testSkipLastThreeLines(tempDir: File) {
        val result = getResult("src/test/resources/skipLastThreeLines.txt", tempDir)
        lines.subList(0, lines.size - 3).forEach { Assertions.assertThat(result).contains(it) }
        lines.subList(lines.size - 3, lines.size).forEach { Assertions.assertThat(result).doesNotContain(it) }
    }

    @Test
    fun testInvalidSkipLines(tempDir: File) {
        val result = getResult("src/test/resources/invalidSkipLines.txt", tempDir)
        (lines.subList(0, 3) + lines.subList(4, lines.size)).forEach { Assertions.assertThat(result).contains(it) }
        Assertions.assertThat(result).doesNotContain(lines[3])
    }

    @Test
    fun testSkipOverlapping(tempDir: File) {
        val result = getResult("src/test/resources/skipOverlapping.txt", tempDir)
        (lines.subList(0, 2) + lines.subList(6, lines.size)).forEach { Assertions.assertThat(result).contains(it) }
        lines.subList(2, 6).forEach { Assertions.assertThat(result).doesNotContain(it) }
    }

    @Test
    fun testNoFileProvided(tempDir: File) {
        val result = getResult("", tempDir, false)
        Assertions.assertThat(result).contains("No file provided.")
    }

    @Test
    fun testFileNotFound(tempDir: File) {
        val result = getResult("src/test/resources/nonexistentFile.txt", tempDir)
        Assertions.assertThat(result).contains("File src/test/resources/nonexistentFile.txt not found.")
    }

    private fun getResult(filename: String, tempDir: File, addFilename: Boolean = true): String {
        val filenameString = if (addFilename) "filename = \"$filename\"" else ""
        File(tempDir, "build.gradle.kts").run {
            writeText(
                """
                        plugins {
                            id("ru.hse.spb.sharkova.printfile")
                        }
                        
                        printfile {
                            enabled = true
                            $filenameString
                        }
                        """
            )
        }

        val buildResult = GradleRunner.create()
            .withProjectDir(tempDir)
            .withPluginClasspath()
            .withArguments("printfile")
            .build()

        return buildResult.output
    }
}