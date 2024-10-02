package backend.academy.data;

import backend.academy.data.enums.MazeGenerationAlgorithm;
import backend.academy.data.enums.PathfindingAlgorithm;
import backend.academy.data.maze.CellType;
import backend.academy.data.maze.Point;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
public final class GameSettings {
    public static final GameSettings DEFAULT_SETTINGS = builder().build();

    @Default
    private final List<CellType> additionalTypes = List.of(
        new CellType(2, true, 5, '▓'),
        new CellType(3, true, -10, '₿')
    );

    @Default
    private final Character pathRender = '·';

    @Default
    private final Character wallRender = '█';

    @Default
    private final Character passageRender = '░';

    @Default
    private final Integer mazeHeight = 20;

    @Default
    private final Integer mazeWidth = 20;

    @Default
    private final MazeGenerationAlgorithm generationAlgorithm = PRIM;

    @Default
    private final PathfindingAlgorithm pathfindingAlgorithm = BELLMAN;

    @Default
    private final Integer pathRenderSpeedMs = 500;

    @Default
    private final Point start = Point.of(0, 0);

    @Default
    private final Point end = Point.of(1, 1);

    public MutableGameSettings mutable() {
        return MutableGameSettings.builder()
            .additionalTypes(additionalTypes)
            .pathRender(pathRender)
            .wallRender(wallRender)
            .passageRender(passageRender)
            .mazeHeight(mazeHeight)
            .mazeWidth(mazeWidth)
            .generationAlgorithm(generationAlgorithm)
            .pathfindingAlgorithm(pathfindingAlgorithm)
            .pathRenderSpeedMs(pathRenderSpeedMs)
            .start(start)
            .end(end)
            .build();
    }

    /**
     * Validates the game settings
     *
     * @return true if the settings are invalid, false otherwise
     */
    public boolean isInvalid() {
        return mazeHeight() < 1
            || mazeWidth() < 1
            || start.col() < 0 || start.row() < 0
            || end.col() < 0 || end.row() < 0
            || start.col() >= mazeWidth()
            || start.row() >= mazeHeight()
            || end.col() >= mazeWidth()
            || end.row() >= mazeHeight()
            || start.equals(end);
    }
}
