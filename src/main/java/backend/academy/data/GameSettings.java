package backend.academy.data;

import backend.academy.data.enums.MazeGenerationAlgorithm;
import backend.academy.data.enums.PathfindingAlgorithm;
import backend.academy.data.maze.CellType;
import backend.academy.data.maze.Point;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import static backend.academy.data.enums.MazeGenerationAlgorithm.PRIM;
import static backend.academy.data.enums.PathfindingAlgorithm.BELLMAN;
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
        new CellType(true, 5, '░'),
        new CellType(true, -10, '₿')
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
    private final PathfindingAlgorithm pathfindingAlgorithm = BELLMAN;

    @JsonProperty
    @Default
    private final Integer pathRenderSpeedMs = 500;

    @JsonProperty
    @Default
    private final Point start = Point.of(0, 0);

    @JsonProperty
    @Default
    private final Point end = Point.of(1, 1);

    @JsonProperty
    @Default
    private final Double biomesFreq = 1.0;

    /**
     * Validates the game settings
     *
     * @return true if the settings are invalid, false otherwise
     */
    @JsonIgnore
    public boolean isInvalid() {
        return additionalTypes == null
            || pathRender == null
            || wallRender == null
            || passageRender == null
            || startRender == null
            || endRender == null
            || generationAlgorithm == null
            || pathfindingAlgorithm == null
            || mazeHeight == null
            || mazeWidth == null
            || start == null
            || end == null
            || biomesFreq == null
            || pathRenderSpeedMs == null
            || mazeHeight() < 1
            || mazeWidth() < 1
            || start.col() < 0 || start.row() < 0
            || end.col() < 0 || end.row() < 0
            || start.col() >= mazeWidth()
            || start.row() >= mazeHeight()
            || end.col() >= mazeWidth()
            || end.row() >= mazeHeight()
            || start.equals(end)
            || biomesFreq < 0.0
            || pathRenderSpeedMs < 0;
    }
}
