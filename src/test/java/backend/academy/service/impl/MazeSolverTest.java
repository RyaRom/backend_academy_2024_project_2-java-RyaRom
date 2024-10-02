package backend.academy.service.impl;

import backend.academy.data.GameSettings;
import backend.academy.data.GameSettingsMutable;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static backend.academy.data.enums.PathfindingAlgorithm.BFS;
import static backend.academy.data.enums.PathfindingAlgorithm.DFS;

class MazeSolverTest {

    private MazeSolver mazeSolver;

    private MazeRenderer mazeRenderer;

    private MazeGenerator mazeGenerator;

    private GameSettingsMutable settingsMutable;

    private Maze maze;

    @BeforeEach
    void setUp() {
        settingsMutable = GameSettingsMutable.builder()
            .mazeHeight(10)
            .mazeWidth(50)
            .start(Point.of(0, 0))
            .end(Point.of(9, 49))
            .pathRenderSpeedMs(100)
            .build();
        GameSettings settings = settingsMutable.toImmutable();
        mazeRenderer = new MazeRenderer(settings, System.out);
        mazeGenerator = new MazeGenerator(settings);
        maze = mazeGenerator.generate();
    }

    @Test
    void bfs() {
        settingsMutable.pathfindingAlgorithm(BFS);
        GameSettings settings = settingsMutable.toImmutable();
        mazeSolver = new MazeSolver(settings);
        mazeRenderer.render(maze);
        var pathBfs = mazeSolver.solve(maze);
        mazeRenderer.render(maze, pathBfs);
    }

    @Test
    void dfs() {
        settingsMutable.pathfindingAlgorithm(DFS);
        GameSettings settings = settingsMutable.toImmutable();
        mazeSolver = new MazeSolver(settings);
        mazeRenderer.render(maze);
        var pathDfs = mazeSolver.solve(maze);
        mazeRenderer.render(maze, pathDfs);
    }
}
