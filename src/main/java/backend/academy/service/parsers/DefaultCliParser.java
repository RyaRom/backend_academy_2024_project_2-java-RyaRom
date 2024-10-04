package backend.academy.service.parsers;

import java.io.BufferedReader;
import java.io.PrintStream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import static backend.academy.game.GameContext.CONSOLE_READER;

@RequiredArgsConstructor
public class DefaultCliParser implements CliParser {

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

    @SneakyThrows @Override
    public String read(String regex) {
        while (inputReader.ready() || inputReader.equals(CONSOLE_READER)) {
            String input = inputReader.readLine();
            if (input == null) {
                break;
            }
            input = input.trim();
            if (input.matches(regex)) {
                return input;
            }
            outputWriter.print(inputNotRecognized);
        }
        return "";
    }
}
