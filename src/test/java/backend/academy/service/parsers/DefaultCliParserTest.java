package backend.academy.service.parsers;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultCliParserTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private final PrintStream outputWriter = new PrintStream(outputStream);

    private static Stream<Arguments> readCommandValidInputTestData() {
        return Stream.of(
            Arguments.of("5\n", 5),
            Arguments.of("1\n", 1),
            Arguments.of("10\n", 10)
        );
    }

    private static Stream<Arguments> readCommandInvalidInputTestData() {
        return Stream.of(
            Arguments.of("abc\n12\n2", 2),
            Arguments.of("15\n", -1),
            Arguments.of("", -1)
        );
    }

    @ParameterizedTest
    @MethodSource({"readCommandValidInputTestData", "readCommandInvalidInputTestData"})
    void readCommand(String stringInput, int expectedValue) throws IOException {
        try (BufferedReader inputInRange = new BufferedReader(new StringReader(stringInput))) {
            assertWith(
                new DefaultCliParser(inputInRange, outputWriter),
                parser -> assertEquals(expectedValue, parser.readCommand(1, 10)));
        }
    }
}
