package backend.academy.service.parsers;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultCliParserTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private final PrintStream outputWriter = new PrintStream(outputStream);

    private DefaultCliParser parser;

    @Test
    void readCommandValidInput() throws IOException {
        try (BufferedReader validInputInRange = new BufferedReader(new StringReader("5\n"));
             BufferedReader validInputLowerBound = new BufferedReader(new StringReader("1\n"));
             BufferedReader validInputUpperBound = new BufferedReader(new StringReader("10\n"))) {

            parser = new DefaultCliParser(validInputInRange, outputWriter);
            Integer resultInRange = parser.readCommand(1, 10);
            parser = new DefaultCliParser(validInputLowerBound, outputWriter);
            Integer resultLowerBound = parser.readCommand(1, 10);
            parser = new DefaultCliParser(validInputUpperBound, outputWriter);
            Integer resultUpperBound = parser.readCommand(1, 10);

            assertEquals(5, resultInRange);
            assertEquals(1, resultLowerBound);
            assertEquals(10, resultUpperBound);
        }
    }

    @Test
    void readCommandInvalidInput() throws IOException {
        try (BufferedReader invalidInputNonNumeric = new BufferedReader(new StringReader("abc\n12\n2"));
             BufferedReader invalidInputOutOfRange = new BufferedReader(new StringReader("15\n"));
             BufferedReader emptyInput = new BufferedReader(new StringReader(""))) {

            parser = new DefaultCliParser(invalidInputNonNumeric, outputWriter);
            Integer resultNonNumeric = parser.readCommand(1, 10);
            parser = new DefaultCliParser(invalidInputOutOfRange, outputWriter);
            Integer resultOutOfRange = parser.readCommand(1, 10);
            parser = new DefaultCliParser(emptyInput, outputWriter);
            Integer resultEmptyInput = parser.readCommand(1, 10);

            assertEquals(2, resultNonNumeric);
            assertEquals(-1, resultOutOfRange);
            assertEquals(-1, resultEmptyInput);
        }
    }

}
