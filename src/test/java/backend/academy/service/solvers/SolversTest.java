package backend.academy.service.solvers;

import backend.academy.data.settings.MutableGameSettings;
import backend.academy.data.maze.CellType;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.service.generators.PrimGenerator;
import backend.academy.service.generators.PrimitiveBiomeGenerator;
import backend.academy.service.renderers.DefaultMazeRenderer;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static backend.academy.data.maze.CellType.PASSAGE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolversTest {
    private MutableGameSettings gameSettings = MutableGameSettings.builder()
        .mazeHeight(10)
        .mazeWidth(10)
        .end(Point.of(9, 9))
        .pathRenderSpeedMs(100)
        .wallRender('0')
        .pathRender('2')
        .passageRender('p')
        .startRender('s')
        .endRender('e')
        .additionalTypes(
            List.of(
                new CellType(true, 2, 'b'),
                new CellType(true, 0, 'g')
            ))
        .build();

    private final Maze maze =
        new PrimGenerator(gameSettings.immutable(), new PrimitiveBiomeGenerator(gameSettings.immutable())).generate();

    private DefaultMazeRenderer renderer;

    @BeforeEach
    void setUp() {
        renderer = new DefaultMazeRenderer(gameSettings.immutable(), System.out);
    }

    @Test
    void bfs() {
        renderer.render(maze);
        BfsSolver solver = new BfsSolver(gameSettings.immutable());
        var path = solver.solve(maze);

        assertFalse(path.isEmpty());
        assertTrue(path.contains(gameSettings.start()));
        assertTrue(path.contains(gameSettings.end()));
        assertTrue(path.size() >= gameSettings.mazeHeight() + gameSettings.mazeWidth() - 2);
        renderer.render(maze, path);
    }

    @Test
    void dfs() {
        renderer.render(maze);
        DfsSolver solver = new DfsSolver(gameSettings.immutable());
        var path = solver.solve(maze);

        assertFalse(path.isEmpty());
        assertTrue(path.contains(gameSettings.start()));
        assertTrue(path.contains(gameSettings.end()));
        assertTrue(path.size() >= gameSettings.mazeHeight() + gameSettings.mazeWidth() - 2);
        renderer.render(maze, path);
    }

    @Test
    void bellman() {
        renderer.render(maze);
        BellmanSolver solver = new BellmanSolver(gameSettings.immutable());
        var path = solver.solve(maze);

        assertFalse(path.isEmpty());
        assertTrue(path.contains(gameSettings.start()));
        assertTrue(path.contains(gameSettings.end()));
        assertTrue(path.size() >= gameSettings.mazeHeight() + gameSettings.mazeWidth() - 2);
        renderer.render(maze, path);
    }

    @Test
    void spfa() {
        gameSettings = MutableGameSettings.builder()
            .mazeHeight(10)
            .mazeWidth(10)
            .end(Point.of(9, 9))
            .pathRenderSpeedMs(0)
            .build();
        renderer = new DefaultMazeRenderer(gameSettings.immutable(), System.out);
        renderer.render(maze);
        SpfaSolver solver = new SpfaSolver(gameSettings.immutable());
        var path = solver.solve(maze);

        assertFalse(path.isEmpty());
        assertTrue(path.contains(gameSettings.start()));
        assertTrue(path.contains(gameSettings.end()));
        assertTrue(path.size() >= gameSettings.mazeHeight() + gameSettings.mazeWidth() - 2);
        renderer.render(maze, path);
    }

    @Test
    void allPaths() {
        gameSettings = MutableGameSettings.builder()
            .mazeHeight(3)
            .mazeWidth(3)
            .end(Point.of(2, 2))
            .pathRenderSpeedMs(0)
            .build();
        renderer = new DefaultMazeRenderer(gameSettings.immutable(), System.out);
        var best = new CellType(true, -2, 's');
        var good = new CellType(true, 0, 'g');
        var bad = new CellType(true, 2, 'b');
        Maze maze = new Maze(3, 3);
        maze.setCell(Point.of(0, 0), PASSAGE);
        maze.setCell(Point.of(0, 1), good);
        maze.setCell(Point.of(0, 2), best);
        maze.setCell(Point.of(1, 0), bad);
        maze.setCell(Point.of(1, 1), bad);
        maze.setCell(Point.of(1, 2), best);
        maze.setCell(Point.of(2, 0), bad);
        maze.setCell(Point.of(2, 1), bad);
        maze.setCell(Point.of(2, 2), PASSAGE);
        renderer.render(maze);

        SpfaSolver solver = new SpfaSolver(gameSettings.immutable());
        var path = solver.solve(maze);
        renderer.render(maze, path);

        assertTrue(path.contains(Point.of(0, 0)));
        assertTrue(path.contains(Point.of(0, 1)));
        assertTrue(path.contains(Point.of(0, 2)));
        assertTrue(path.contains(Point.of(1, 2)));
        assertTrue(path.contains(Point.of(2, 2)));
        assertFalse(path.contains(Point.of(1, 0)));
        assertFalse(path.contains(Point.of(1, 1)));
        assertFalse(path.contains(Point.of(2, 0)));
        assertFalse(path.contains(Point.of(2, 1)));
    }
}
