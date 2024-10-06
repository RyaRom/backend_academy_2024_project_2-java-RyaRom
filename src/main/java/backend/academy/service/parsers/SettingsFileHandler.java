package backend.academy.service.parsers;

import backend.academy.data.enums.MazeGenerationAlgorithm;
import backend.academy.data.enums.PathfindingAlgorithm;
import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.gameSettings.MutableGameSettings;
import backend.academy.data.maze.CellType;
import backend.academy.data.maze.Point;
import backend.academy.service.renderers.CliRenderer;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static backend.academy.utils.Randomizer.getRandomInt;

@Getter
@SuppressWarnings("MultipleStringLiterals")
@RequiredArgsConstructor
public class SettingsFileHandler implements FileHandler {
    private final CliParser parser;

    private final CliRenderer renderer;

    private final FileParser<GameSettings> fileParser;

    private final MutableGameSettings gameSettings = MutableGameSettings.builder().build();

    @SuppressWarnings("MagicNumber")
    private String name = String.valueOf(getRandomInt(100000));

    @Override public void createSettings() {
        configureDigit("maze height", gameSettings::mazeHeight);
        configureDigit("maze width", gameSettings::mazeWidth);
        configureDigit("path render speed in ms", gameSettings::pathRenderSpeedMs);
        configureDouble("biomes frequency (2.0 is optimal max here)", gameSettings::biomesFreq);
        configureSingleCharacter("path render", gameSettings::pathRender);
        configureSingleCharacter("wall render", gameSettings::wallRender);
        configureSingleCharacter("passage render", gameSettings::passageRender);
        configureSingleCharacter("start render", gameSettings::startRender);
        configureSingleCharacter("end render", gameSettings::endRender);
        configureGenAlgorithm();
        configurePathAlgorithm();
        configureStartPoint();
        configureEndPoint();
        configureAdditionalTypes();
    }

    private void configureDigit(String name, Consumer<Integer> setter) {
        renderer.println("Enter %s (leave blank for default)".formatted(name));
        var input = parser.read("^\\d*");
        if (!input.isBlank()) {
            try {
                setter.accept(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                renderer.println("Invalid input. Default will be used.");
            }
        }
    }

    private void configureDouble(String name, Consumer<Double> setter) {
        renderer.println("Enter %s (leave blank for default)".formatted(name));
        var input = parser.read("^(\\d+(\\.\\d*)?)|\\s*$");
        if (!input.isBlank()) {
            try {
                setter.accept(Double.parseDouble(input));
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

    @Override public void changeName() {
        renderer.println("Enter new file name:");
        String newName = parser.read("[a-zA-Z]*");
        if (!newName.isBlank()) {
            name = newName;
        }
    }

    @Override public void save(String path) {
        File newJson = new File(
            path + "/" + name + "_settings.json"
        );
        fileParser.writeToFile(gameSettings.immutable(), newJson);
    }
}
