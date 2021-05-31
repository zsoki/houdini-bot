package hu.zsoki.houdinibot.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class CommandMessageTest {

    private fun commandMessageTestParams(): Stream<TestParam> = Stream.of(
        TestParam(
            input = ";help valami",
            dropWords = 1,
            expectedWords = listOf(";help", "valami"),
            expectedDropWords = "valami"
        ),
        TestParam(
            input = ";quoteadd parancs Ez   egy   quote",
            dropWords = 1,
            expectedWords = listOf(";quoteadd", "parancs", "Ez", "egy", "quote"),
            expectedDropWords = "parancs Ez   egy   quote"
        ),
        TestParam(
            input = ";quoteadd parancs Ez   egy   quote",
            dropWords = 2,
            expectedWords = listOf(";quoteadd", "parancs", "Ez", "egy", "quote"),
            expectedDropWords = "Ez   egy   quote"
        ),
        TestParam(
            input = ";invite",
            dropWords = 1,
            expectedWords = listOf(";invite"),
            expectedDropWords = ""
        ),
    )

    @ParameterizedTest
    @MethodSource("commandMessageTestParams")
    fun `Should correctly create command message`(testParam: TestParam) {
        val commandMessage = CommandMessage(testParam.input)

        assertIterableEquals(testParam.expectedWords, commandMessage.words)
        assertEquals(testParam.expectedDropWords, commandMessage.dropWords(testParam.dropWords))
    }

    data class TestParam(
        val input: String,
        val dropWords: Int,
        val expectedWords: List<String>,
        val expectedDropWords: String
    )
}