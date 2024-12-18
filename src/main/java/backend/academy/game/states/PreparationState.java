package backend.academy.game.states;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.game.GameContext;
import backend.academy.service.factories.FileHandlerFactory;
import backend.academy.service.factories.GeneratorFactory;
import backend.academy.service.factories.SolverFactory;
import backend.academy.service.parsers.CliParser;
import backend.academy.service.parsers.FileHandler;
import backend.academy.service.parsers.FileParser;
import backend.academy.service.renderers.CliRenderer;
import backend.academy.service.renderers.DefaultMazeRenderer;
import backend.academy.service.renderers.MazeRenderer;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static backend.academy.game.GameContext.SETTINGS_MENU;
import static backend.academy.game.GameContext.START_MENU;

/**
 * This class is representing preparation state of the game
 * <p>
 * This state is used to load settings,create game configuration and validate chosen settings
 * </p>
 */
@RequiredArgsConstructor
@Log4j2
@SuppressWarnings({"MagicNumber", "MultipleStringLiterals"})
public class PreparationState implements GameState {
    public final static String SETTINGS_ERROR = "Invalid settings, load new settings or set back to default";

    private final CliRenderer renderer;

    private final CliParser parser;

    private final FileParser<GameSettings> fileParser;

    private final File settingsLocation;

    private final FileHandlerFactory fileHandlerFactory;

    private GameSettings newSettings;

    @Override
    public void gameCycle(GameContext gameContext) {
        renderer.clearScreen();
        if (gameContext.terminate()) {
            return;
        }
        if (newSettings == null) {
            newSettings = gameContext.defaultSettings();
        }
        renderer.renderIntro();
        renderer.renderMenu(START_MENU);
        var validations = newSettings.isInvalid();
        if (!validations.isEmpty()) {
            renderer.println(SETTINGS_ERROR
                + System.lineSeparator()
                + validations.stream()
                    .collect(Collectors.joining(System.lineSeparator())));
        }

        int command = parser.readCommand(0, START_MENU.size());
        switch (command) {
            case 0 -> gameContext.exit();
            case 1 -> nextState(gameContext);
            case 2 -> loadSettingsSelector(gameContext);
            case 3 -> {
                FileHandler settingsFileHandler =
                    fileHandlerFactory.fileHandler(parser, renderer, fileParser);
                loadSettingsCreator(gameContext, settingsFileHandler);
            }
            default -> throw new IllegalStateException("Unexpected value: " + command);
        }
    }

    private void loadSettingsSelector(GameContext gameContext) {
        List<String> allSettings = new ArrayList<>();
        allSettings.add("Load default settings");
        allSettings.addAll(Arrays.stream(fileParser.getJsonInDir(settingsLocation)).toList());
        renderer.renderMenu(allSettings, "settings (path %s)".formatted(settingsLocation));

        int menuChoice = parser.readCommand(0, allSettings.size());
        switch (menuChoice) {
            case 0 -> {
                gameCycle(gameContext);
                return;
            }
            case 1 -> newSettings = gameContext.defaultSettings();
            default -> {
                String chosenPath = settingsLocation + "/" + allSettings.get(menuChoice - 1);
                log.info("Adding words from JSON file: {}", chosenPath);
                newSettings = fileParser.readFromFile(new File(chosenPath), GameSettings.class);
            }
        }
        gameCycle(gameContext);
    }

    @SuppressWarnings("ReturnCount")
    private void loadSettingsCreator(GameContext gameContext, FileHandler settingsFileHandler) {
        renderer.renderMenu(SETTINGS_MENU);

        int menuChoice = parser.readCommand(0, SETTINGS_MENU.size());
        switch (menuChoice) {
            case 0 -> {
                gameCycle(gameContext);
                return;
            }
            case 1 -> settingsFileHandler.createSettings();
            case 2 -> settingsFileHandler.changeName();
            case 3 -> {
                settingsFileHandler.save(settingsLocation.getPath());
                gameCycle(gameContext);
                return;
            }
            default -> throw new IllegalStateException("Unexpected value: " + menuChoice);
        }
        loadSettingsCreator(gameContext, settingsFileHandler);
    }

    @Override
    public void nextState(GameContext gameContext) {
        if (!newSettings.isInvalid().isEmpty()) {
            gameCycle(gameContext);
            return;
        }
        log.info("Game settings is {}. Game is configured", newSettings.toString());
        gameContext.defaultSettings(newSettings);
        GeneratorFactory generatorFactory = new GeneratorFactory(newSettings);
        SolverFactory solverFactory = new SolverFactory(newSettings);
        MazeRenderer mazeRenderer = new DefaultMazeRenderer(newSettings, gameContext.outputWriter());

        gameContext.start(solverFactory.solver(),
            generatorFactory.generator(),
            renderer,
            parser,
            mazeRenderer
        );
    }
}
