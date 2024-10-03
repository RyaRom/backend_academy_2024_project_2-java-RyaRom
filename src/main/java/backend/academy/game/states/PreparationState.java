package backend.academy.game.states;

import backend.academy.data.GameSettings;
import backend.academy.game.GameContext;
import backend.academy.game.GameState;
import backend.academy.game.SettingsFileHandler;
import backend.academy.service.CliParser;
import backend.academy.service.CliRenderer;
import backend.academy.service.FileParser;
import backend.academy.service.factories.GeneratorFactory;
import backend.academy.service.factories.SolverFactory;
import backend.academy.service.renderers.DefaultMazeRenderer;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static backend.academy.data.GameSettings.DEFAULT_SETTINGS;
import static backend.academy.game.GameContext.SETTINGS_LOCATION;
import static backend.academy.game.GameContext.SETTINGS_MENU;
import static backend.academy.game.GameContext.START_MENU;

@RequiredArgsConstructor
@Log4j2
@SuppressWarnings({"MagicNumber", "MultipleStringLiterals"})
public class PreparationState implements GameState {
    private final static String SETTINGS_ERROR = "Invalid settings, load new settings or set back to default";

    private final CliRenderer renderer;

    private final CliParser parser;

    private final FileParser<GameSettings> fileParser;

    private GameSettings gameSettings = DEFAULT_SETTINGS;

    @Override
    public void gameCycle(GameContext gameContext) {
        renderer.clearScreen();
        if (gameContext.terminate()) {
            return;
        }
        if (gameSettings.isInvalid()) {
            renderer.println(SETTINGS_ERROR);
        }
        renderer.renderIntro();
        renderer.renderMenu(START_MENU);

        int command = parser.readCommand(0, START_MENU.size());
        switch (command) {
            case 0 -> gameContext.exit();
            case 1 -> nextState(gameContext);
            case 2 -> loadSettingsSelector(gameContext);
            case 3 -> {
                SettingsFileHandler settingsFileHandler = new SettingsFileHandler(
                    parser, renderer, fileParser
                );
                loadSettingsCreator(gameContext, settingsFileHandler);
            }
            default -> throw new IllegalStateException("Unexpected value: " + command);
        }
    }

    private void loadSettingsSelector(GameContext gameContext) {
        List<String> allSettings = new ArrayList<>();
        allSettings.add("Load default settings");
        allSettings.addAll(Arrays.stream(fileParser.getJsonInDir(SETTINGS_LOCATION)).toList());
        renderer.renderMenu(allSettings, "settings (path %s)".formatted(SETTINGS_LOCATION));

        int menuChoice = parser.readCommand(0, allSettings.size());
        switch (menuChoice) {
            case 0 -> gameCycle(gameContext);
            case 1 -> gameSettings = DEFAULT_SETTINGS;
            default -> {
                String chosenPath = SETTINGS_LOCATION + allSettings.get(menuChoice - 1);
                log.info("Adding words from JSON file: {}", chosenPath);
                gameSettings = fileParser.readFromFile(new File(chosenPath), GameSettings.class);
            }
        }
        gameCycle(gameContext);
    }

    private void loadSettingsCreator(GameContext gameContext, SettingsFileHandler settingsFileHandler) {
        renderer.renderMenu(SETTINGS_MENU);

        int menuChoice = parser.readCommand(0, SETTINGS_MENU.size());
        switch (menuChoice) {
            case 0 -> gameCycle(gameContext);
            case 1 -> settingsFileHandler.createSettings();
            case 2 -> settingsFileHandler.changeName();
            case 3 -> {
                settingsFileHandler.save(SETTINGS_LOCATION.getPath());
                gameCycle(gameContext);
                return;
            }
            default -> throw new IllegalStateException("Unexpected value: " + menuChoice);
        }
        loadSettingsCreator(gameContext, settingsFileHandler);
    }

    @Override
    public void nextState(GameContext gameContext) {
        if (gameSettings.isInvalid()) {
            gameCycle(gameContext);
            return;
        }
        GeneratorFactory generatorFactory = new GeneratorFactory(gameSettings);
        SolverFactory solverFactory = new SolverFactory(gameSettings);

        InProgressState newState = new InProgressState(
            renderer,
            parser,
            new DefaultMazeRenderer(gameSettings, gameContext.outputWriter()),
            generatorFactory.generator(),
            solverFactory.solver()
        );
        gameContext.state(newState);
        gameContext.state().gameCycle(gameContext);
    }
}
