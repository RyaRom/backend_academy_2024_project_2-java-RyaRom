package backend.academy.game;

import backend.academy.data.GameSettings;
import backend.academy.data.MutableGameSettings;
import backend.academy.data.enums.MazeGenerationAlgorithm;
import backend.academy.data.enums.PathfindingAlgorithm;
import backend.academy.data.maze.CellType;
import backend.academy.data.maze.Point;
import backend.academy.service.CliParser;
import backend.academy.service.CliRenderer;
import backend.academy.service.FileParser;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import static backend.academy.utils.Randomizer.getRandomInt;

@SuppressWarnings("MultipleStringLiterals")
@RequiredArgsConstructor
public final class SettingsFileHandler {
    private final CliParser parser;

    private final CliRenderer renderer;

    private final FileParser<GameSettings> fileParser;

    private final MutableGameSettings gameSettings = MutableGameSettings.builder().build();

    private String name = getRandomInt();

    public void createSettings() {
        configureDigit("maze height", gameSettings::mazeHeight);
        configureDigit("maze width", gameSettings::mazeWidth);
        configureDigit("path render speed in ms", gameSettings::pathRenderSpeedMs);
        configureSingleCharacter("pathRender", gameSettings::pathRender);
        configureSingleCharacter("wallRender", gameSettings::wallRender);
        configureSingleCharacter("passageRender", gameSettings::passageRender);
        configureGenAlgorithm();
        configurePathAlgorithm();
        configureStartPoint();
        configureEndPoint();
        configureAdditionalTypes();
    }

    private void configureDigit(String name, Consumer<Integer> setter) {
        renderer.println("Enter %s (leave blank for default):%n".formatted(name));
        var input = parser.read("\\d*");
        if (!input.isBlank()) {
            try {
                setter.accept(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                renderer.println("Invalid input. Default will be used.");
            }
        }
    }

    private void configureSingleCharacter(String settingName, Consumer<Character> setter) {
        renderer.println("Enter %s (leave blank for default)".formatted(settingName));
        var input = parser.read(".?");
        if (!input.isBlank()) {
            setter.accept(input.charAt(0));
        }
    }

    private void configureGenAlgorithm() {
        renderer.println("Enter generation algorithm (leave blank for default)");
        renderer.println("Options: " + Arrays.toString(MazeGenerationAlgorithm.values()));
        var input = parser.read(".*");
        if (!input.isBlank()) {
            try {
                gameSettings.generationAlgorithm(MazeGenerationAlgorithm.valueOf(input.toUpperCase()));
            } catch (IllegalArgumentException e) {
                renderer.println("Invalid algorithm. Default will be used.");
            }
        }
    }

    private void configurePathAlgorithm() {
        renderer.println("Enter pathfinding algorithm (leave blank for default)");
        renderer.println("Options: " + Arrays.toString(PathfindingAlgorithm.values()));
        var input = parser.read(".*");
        if (!input.isBlank()) {
            try {
                gameSettings.pathfindingAlgorithm(PathfindingAlgorithm.valueOf(input.toUpperCase()));
            } catch (IllegalArgumentException e) {
                renderer.println("Invalid algorithm. Default will be used.");
            }
        }
    }

    private void configureStartPoint() {
        renderer.println("Enter start point (x y) (leave blank for default):");
        var input = parser.read("(\\d+ \\d+)|( *)");
        if (!input.isBlank()) {
            try {
                String[] parts = input.split(" ");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                gameSettings.start(Point.of(x, y));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                renderer.println("Invalid input. Default will be used.");
            }
        }
    }

    private void configureEndPoint() {
        renderer.println("Enter end point (x y) (leave blank for default):");
        var input = parser.read("(\\d+ \\d+)|( *)");
        if (!input.isBlank()) {
            try {
                String[] parts = input.split(" ");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                gameSettings.end(Point.of(x, y));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                renderer.println("Invalid input. Default will be used.");
            }
        }
    }

    private void configureAdditionalTypes() {
        renderer.println("Would you like to add custom cell types? (y/n):");
        var input = parser.read("[yn]");
        if (input.equalsIgnoreCase("y")) {
            gameSettings.additionalTypes(new ArrayList<>());
            while (true) {
                try {
                    renderer.println("Is the cell passable? (true/false):");
                    var passableInput = parser.read("true|false");
                    boolean passable = Boolean.parseBoolean(passableInput);

                    renderer.println("Enter cell type score (integer):");
                    var scoreInput = parser.read("-?\\d+");
                    int score = Integer.parseInt(scoreInput);

                    renderer.println("Enter cell render character:");
                    var renderCharInput = parser.read(".?");
                    char renderChar = renderCharInput.charAt(0);

                    gameSettings.additionalTypes().add(new CellType(passable, score, renderChar));

                    renderer.println("Add another cell type? (y/n):");
                    var continueInput = parser.read("[yn]");
                    if (continueInput.equalsIgnoreCase("n")) {
                        break;
                    }
                } catch (NumberFormatException ignore) {
                }
            }
        }
    }

    public void changeName() {
        renderer.println("Enter new file name:");
        String newName = parser.read("[a-zA-Z]*");
        if (!newName.isBlank()) {
            name = newName;
        }
    }

    public void save(String path) {
        File newJson = new File(
            path + "/" + name + "_settings.json"
        );
        fileParser.writeToFile(gameSettings.immutable(), newJson);
    }
}