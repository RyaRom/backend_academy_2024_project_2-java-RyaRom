package backend.academy.service.solvers;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.service.generators.PrimGenerator;
import backend.academy.service.generators.PrimitiveBiomeGenerator;
import backend.academy.service.renderers.DefaultMazeRenderer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolversTest {
    private final GameSettings gameSettings = GameSettings.builder()
        .mazeHeight(10)
        .mazeWidth(10)
        .end(Point.of(9, 9))
        .pathRenderSpeedMs(100)
        .wallRender('0')
        .pathRender('2')
        .build();

    private final Maze maze = new PrimGenerator(gameSettings, new PrimitiveBiomeGenerator(gameSettings)).generate();

    private final DefaultMazeRenderer renderer = new DefaultMazeRenderer(gameSettings, System.out);

    @Test
    void bfs() {
        renderer.render(maze);
        BfsSolver solver = new BfsSolver(gameSettings);
        var path = solver.solve(maze);

        assertFalse(path.isEmpty());
        assertTrue(path.contains(gameSettings.start()));
        assertTrue(path.contains(gameSettings.end()));
        assertTrue(path.size() >= gameSettings.mazeHeight() + gameSettings.mazeWidth() - 2);
    }

    @Test
    void dfs() {
        renderer.render(maze);
        DfsSolver solver = new DfsSolver(gameSettings);
        var path = solver.solve(maze);

        assertFalse(path.isEmpty());
        assertTrue(path.contains(gameSettings.start()));
        assertTrue(path.contains(gameSettings.end()));
        assertTrue(path.size() >= gameSettings.mazeHeight() + gameSettings.mazeWidth() - 2);
    }

    @Test
    void allPaths() {
        renderer.render(maze);
        BfsSolver bfsSolver = new BfsSolver(gameSettings);
        DfsSolver dfsSolver = new DfsSolver(gameSettings);
        var bfsPath = bfsSolver.solve(maze);
        var dfsPath = dfsSolver.solve(maze);

        assertFalse(bfsPath.isEmpty());
        assertFalse(dfsPath.isEmpty());
        assertTrue(bfsPath.size() <= dfsPath.size());

        renderer.render(maze, bfsPath);
        renderer.render(maze, dfsPath);
    }
}
