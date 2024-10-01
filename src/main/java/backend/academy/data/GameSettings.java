package backend.academy.data;

import backend.academy.data.enums.MazeGenerationAlgorithm;
import backend.academy.data.enums.PathfindingAlgorithm;
import backend.academy.data.maze.CellType;
import backend.academy.data.maze.Point;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import static backend.academy.data.enums.MazeGenerationAlgorithm.PRIM;
import static backend.academy.data.enums.PathfindingAlgorithm.BELLMAN;
import static lombok.Builder.Default;

@Getter
@Builder
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
    private final Character passageRender = ' ';

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
    private final Point end = Point.of(10, 10);
}
