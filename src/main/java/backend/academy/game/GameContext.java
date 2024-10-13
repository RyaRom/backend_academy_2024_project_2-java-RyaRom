package backend.academy.game;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.game.states.GameState;
import backend.academy.game.states.InProgressState;
import backend.academy.game.states.PreparationState;
import backend.academy.service.factories.FileHandlerFactory;
import backend.academy.service.generators.Generator;
import backend.academy.service.parsers.CliParser;
import backend.academy.service.parsers.DefaultCliParser;
import backend.academy.service.parsers.JsonParser;
import backend.academy.service.renderers.CliRenderer;
import backend.academy.service.renderers.DefaultCliRenderer;
import backend.academy.service.renderers.MazeRenderer;
import backend.academy.service.solvers.Solver;
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
import lombok.extern.log4j.Log4j2;

/**
 * Main class of the game, representing the game context.
 * <p>It contains the current state of the game, the
 * input and output streams, starts and finishes the game loop.
 * </p>
 */
@Getter
@Setter
@Log4j2
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

    private GameSettings defaultSettings;

    private boolean terminate = false;

    public void init(GameSettings defaultSettings) {
        log.info("Game is started");
        this.defaultSettings = defaultSettings;
        state = new PreparationState(
            new DefaultCliRenderer(outputWriter),
            new DefaultCliParser(inputReader, outputWriter),
            new JsonParser<>(new ObjectMapper()),
            SETTINGS_LOCATION,
            new FileHandlerFactory()
        );
        state.gameCycle(this);
    }

    public void start(
        Solver solver,
        Generator generator,
        CliRenderer renderer,
        CliParser parser,
        MazeRenderer mazeRenderer
    ) {
        state = new InProgressState(
            renderer,
            parser,
            mazeRenderer,
            generator,
            solver
        );
        state.gameCycle(this);
    }

    @SneakyThrows public void exit() {
        log.info("Game is finished");
        inputReader.close();
        outputWriter.close();
        terminate = true;
    }
}
