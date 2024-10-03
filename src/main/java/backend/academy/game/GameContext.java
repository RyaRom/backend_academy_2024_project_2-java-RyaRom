package backend.academy.game;

import backend.academy.data.GameSettings;
import backend.academy.game.states.PreparationState;
import backend.academy.service.parsers.DefaultCliParser;
import backend.academy.service.parsers.JsonParser;
import backend.academy.service.renderers.DefaultCliRenderer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintStream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import static backend.academy.data.GameSettings.DEFAULT_SETTINGS;

@Getter
@Setter
@RequiredArgsConstructor
public class GameContext {
    public static final File SETTINGS_LOCATION = new File("src/main/resources/customWords/");

    private final BufferedReader inputReader;

    private final PrintStream outputWriter;

    private boolean terminate = false;

    private GameSettings gameSettings = DEFAULT_SETTINGS;

    public void init() {
        PreparationState state = new PreparationState(
            new DefaultCliRenderer(outputWriter),
            new DefaultCliParser(inputReader, outputWriter),
            new JsonParser<>(new ObjectMapper())
        );
        state.gameCycle(this);
    }

    @SneakyThrows public void exit() {
        inputReader.close();
        outputWriter.close();
        terminate = true;
    }
}
