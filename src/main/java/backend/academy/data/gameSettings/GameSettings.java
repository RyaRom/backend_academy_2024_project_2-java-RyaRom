package backend.academy.data.gameSettings;

import backend.academy.data.enums.MazeGenerationAlgorithm;
import backend.academy.data.enums.PathfindingAlgorithm;
import backend.academy.data.maze.CellType;
import backend.academy.data.maze.Point;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import static backend.academy.data.enums.MazeGenerationAlgorithm.PRIM;
import static backend.academy.data.enums.PathfindingAlgorithm.SPFA;
import static lombok.Builder.Default;

/**
 * Immutable class for game settings representation.
 * Contains maze width, height, generation algorithm, pathfinding algorithm, render symbols,
 * additional cell types, start and end points, and path rendering speed in ms.
 *
 * <p>Can be saved and written in game menu</p>
 */
@Getter
@Builder
@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
public final class GameSettings {
    public static final GameSettings DEFAULT_SETTINGS = builder().build();

    @JsonProperty
    @Default
    private final List<CellType> additionalTypes = List.of(
        new CellType(true, 10, '░'),
        new CellType(true, 1, '₿')
    );

    @JsonProperty
    @Default
    private final Character pathRender = '·';

    @JsonProperty
    @Default
    private final Character wallRender = '█';

    @JsonProperty
    @Default
    private final Character passageRender = ' ';

    @JsonProperty
    @Default
    private final Character startRender = 'S';

    @JsonProperty
    @Default
    private final Character endRender = 'F';

    @JsonProperty
    @Default
    private final Integer mazeHeight = 20;

    @JsonProperty
    @Default
    private final Integer mazeWidth = 20;

    @JsonProperty
    @Default
    private final MazeGenerationAlgorithm generationAlgorithm = PRIM;

    @JsonProperty
    @Default
    private final PathfindingAlgorithm pathfindingAlgorithm = SPFA;

    @JsonProperty
    @Default
    private final Integer pathRenderSpeedMs = 100;

    @JsonProperty
    @Default
    private final Point start = Point.of(0, 0);

    @JsonProperty
    @Default
    private final Point end = Point.of(9, 9);

    @JsonProperty
    @Default
    private final Double biomesFreq = 1.0;

    @JsonIgnore
    @SuppressWarnings("CyclomaticComplexity")
    public List<String> isInvalid() {
        List<String> violations = new ArrayList<>();
        if (additionalTypes == null || additionalTypes.isEmpty()) {
            violations.add("No additional types specified");
        }
        if (pathRender == null) {
            violations.add("No path render specified");
        }
        if (wallRender == null) {
            violations.add("No wall render specified");
        }
        if (passageRender == null) {
            violations.add("No passage render specified");
        }
        if (startRender == null) {
            violations.add("No start render specified");
        }
        if (endRender == null) {
            violations.add("No end render specified");
        }
        if (generationAlgorithm == null) {
            violations.add("No generation algorithm specified");
        }
        if (pathfindingAlgorithm == null) {
            violations.add("No pathfinding algorithm specified");
        }
        if (pathRenderSpeedMs == null) {
            violations.add("No path render speed specified");
        }
        if (mazeHeight == null || mazeWidth == null) {
            violations.add("Maze dimensions not specified");
        } else {
            if (mazeHeight < 1) {
                violations.add("Maze height must be at least 1");
            }
            if (mazeWidth < 1) {
                violations.add("Maze width must be at least 1");
            }
        }
        if (start == null) {
            violations.add("Start position not specified");
        } else {
            if (start.col() < 0 || start.row() < 0) {
                violations.add("Start position must be non-negative");
            }
            if ((mazeWidth == null ? 0 : mazeWidth) <= start.col()) {
                violations.add("Start column exceeds maze width");
            }
            if ((mazeHeight == null ? 0 : mazeHeight) <= start.row()) {
                violations.add("Start row exceeds maze height");
            }
        }
        if (end == null) {
            violations.add("End position not specified");
        } else {
            if (end.col() < 0 || end.row() < 0) {
                violations.add("End position must be non-negative");
            }
            if (end.col() >= (mazeWidth == null ? 0 : mazeWidth)) {
                violations.add("End column exceeds maze width");
            }
            if (end.row() >= (mazeHeight == null ? 0 : mazeHeight)) {
                violations.add("End row exceeds maze height");
            }
            if (start != null && start.equals(end)) {
                violations.add("Start and end positions must be different");
            }
        }
        if (biomesFreq == null) {
            violations.add("Biome frequency not specified");
        } else if (biomesFreq < 0.0) {
            violations.add("Biome frequency must be non-negative");
        }
        if (pathRenderSpeedMs != null && pathRenderSpeedMs < 0) {
            violations.add("Path render speed must be non-negative");
        }
        if (additionalTypes != null && additionalTypes.stream().anyMatch(c -> c.cost() <= 0)) {
            violations.add("Additional types must have positive cost");
        }
        return violations;
    }
}
