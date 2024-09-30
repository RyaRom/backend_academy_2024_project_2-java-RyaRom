package backend.academy.service.impl;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static backend.academy.data.enums.PathfindingAlgorithm.BFS;

class MazeSolverTest {

    private MazeSolver mazeSolver;

    private MazeRenderer mazeRenderer;

    private MazeGenerator mazeGenerator;

    @BeforeEach
    void setUp() {
        GameSettings settings = GameSettings.builder()
            .pathfindingAlgorithm(BFS)
            .mazeHeight(10)
            .mazeWidth(50)
            .start(Point.of(0, 0))
            .end(Point.of(9, 49))
            .pathRenderSpeedMs(0)
            .build();
        mazeSolver = new MazeSolver(settings);
        mazeRenderer = new MazeRenderer(settings, System.out);
        mazeGenerator = new MazeGenerator(settings);
    }

    @Test
    void solve() {
        var maze = mazeGenerator.generate();
        mazeRenderer.render(maze);
        var path = mazeSolver.solve(maze);
        mazeRenderer.render(maze, path);
    }
}
