package backend.academy.game.states;

import backend.academy.data.GameSettings;
import backend.academy.game.GameContext;
import backend.academy.game.GameState;
import backend.academy.service.CliParser;
import backend.academy.service.CliRenderer;
import backend.academy.service.FileParser;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static backend.academy.data.GameSettings.DEFAULT_SETTINGS;
import static backend.academy.game.GameContext.SETTINGS_LOCATION;

@RequiredArgsConstructor
@Log4j2
public class PreparationState implements GameState {
    private final CliRenderer renderer;

    private final CliParser parser;

    private final FileParser<GameSettings> fileParser;

    private final List<String> startMenu =
        List.of(
            "Generate labyrinth",
            "Load settings configuration",
            "Create new settings configuration"
        );

    private GameSettings gameSettings = DEFAULT_SETTINGS;

    @Override
    public void gameCycle(GameContext gameContext) {
        if (gameContext.terminate()) {
            return;
        }
        renderer.renderIntro();
        renderer.renderMenu(startMenu);

        int command = parser.readCommand(0, startMenu.size() - 1);
        switch (command) {
            case 0 -> gameContext.exit();
            case 1 -> nextState(gameContext);
            case 2 -> loadCustomSettingsSelector(gameContext);
            //Todo case 3 -> create and write new setting json
            default -> throw new IllegalStateException("Unexpected value: " + command);
        }
    }

    private void loadCustomSettingsSelector(GameContext gameContext) {
        List<String> allSettings = new ArrayList<>();
        allSettings.add("Load default settings");
        allSettings.addAll(Arrays.stream(fileParser.getJsonInDir(SETTINGS_LOCATION)).toList());
        renderer.renderMenu(allSettings, "settings (path %s)".formatted(SETTINGS_LOCATION));

        int menuChoice = parser.readCommand(0, allSettings.size() - 1);
        switch (menuChoice) {
            case 0 -> gameCycle(gameContext);
            case 1 -> gameSettings = DEFAULT_SETTINGS;
            default -> {
                String chosenPath = SETTINGS_LOCATION + allSettings.get(menuChoice - 2);
                log.info("Adding words from JSON file: {}", chosenPath);
                gameSettings = fileParser.readFromFile(new File(chosenPath), GameSettings.class);
            }
        }
    }

    @Override
    public void nextState(GameContext gameContext) {

    }
}
