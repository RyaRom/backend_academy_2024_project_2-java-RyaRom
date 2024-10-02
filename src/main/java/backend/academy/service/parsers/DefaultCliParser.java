package backend.academy.service.parsers;

import backend.academy.service.CliParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class DefaultCliParser implements CliParser {
    public final static BufferedReader CONSOLE_READER = new BufferedReader(new InputStreamReader(System.in));

    public final static PrintStream CONSOLE_WRITER = System.out;

    public final String inputNotRecognized = "Input wasn't recognized. Write command again: ";

    private final BufferedReader inputReader;

    private final PrintStream outputWriter;

    @SneakyThrows @Override
    public Integer readCommand(
        int lowerBound,
        int upperBound
    ) {
        while (inputReader.ready() || inputReader.equals(CONSOLE_READER)) {
            String input = inputReader.readLine();
            if (input == null) {
                break;
            }
            input = input.trim();
            if (input.matches("\\d+")) {
                int command = Integer.parseInt(input);
                if (command >= lowerBound && command <= upperBound) {
                    return command;
                }
            }
            outputWriter.print(inputNotRecognized);
        }
        return -1;
    }
}
