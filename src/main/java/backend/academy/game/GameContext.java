package backend.academy.game;

import backend.academy.data.GameSettings;
import backend.academy.game.states.PreparationState;
import backend.academy.service.parsers.DefaultCliParser;
import backend.academy.service.parsers.JsonParser;
import backend.academy.service.renderers.DefaultCliRenderer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import static backend.academy.data.GameSettings.DEFAULT_SETTINGS;

@Getter
@Setter
@RequiredArgsConstructor
public class GameContext {
    public static final File SETTINGS_LOCATION = new File("src/main/resources/settings/");

    public static final List<String> START_MENU =
        List.of(
            "Generate labyrinth",
            "Load settings configuration",
            "Create new settings configuration"
        );

    public static final List<String> SETTINGS_MENU =
        List.of(
            "Create settings configuration",
            "Select name",
            "Save configuration"
        );

    public final static BufferedReader CONSOLE_READER = new BufferedReader(new InputStreamReader(System.in));

    public final static PrintStream CONSOLE_WRITER = System.out;

    private final BufferedReader inputReader;

    private final PrintStream outputWriter;

    private GameState state;

    private boolean terminate = false;

    private GameSettings gameSettings = DEFAULT_SETTINGS;

    public void init() {
        state = new PreparationState(
            new DefaultCliRenderer(outputWriter),
            new DefaultCliParser(inputReader, outputWriter),
            new JsonParser<>(new ObjectMapper()),
            SETTINGS_LOCATION
        );
        state.gameCycle(this);
    }

    @SneakyThrows public void exit() {
        inputReader.close();
        outputWriter.close();
        terminate = true;
    }
}
