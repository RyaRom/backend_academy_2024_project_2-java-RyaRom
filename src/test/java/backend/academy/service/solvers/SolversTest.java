package backend.academy.service.solvers;

import backend.academy.data.gameSettings.MutableGameSettings;
import backend.academy.data.maze.CellType;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.exception.PathNotFoundException;
import backend.academy.service.generators.PrimGenerator;
import backend.academy.service.generators.PrimitiveBiomeGenerator;
import backend.academy.service.renderers.DefaultMazeRenderer;
import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static backend.academy.data.gameSettings.GameSettings.DEFAULT_SETTINGS;
import static backend.academy.data.maze.CellType.PASSAGE;
import static backend.academy.data.maze.CellType.WALL;
import static backend.academy.utils.Randomizer.getRandomInt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

class SolversTest {
    private final Maze maze = new Maze(5, 5);

    private MutableGameSettings gameSettings;

    private DefaultMazeRenderer renderer;

    private PrintStream printStream;

    @BeforeEach
    void setUp() {
        var bad = new CellType(true, 1000, 'b');
        var good = new CellType(true, 0, 'g');
        maze.setCell(Point.of(0, 0), bad);
        maze.setCell(Point.of(0, 1), bad);
        maze.setCell(Point.of(0, 2), bad);
        maze.setCell(Point.of(0, 3), bad);
        maze.setCell(Point.of(0, 4), bad);
        maze.setCell(Point.of(1, 0), good);
        maze.setCell(Point.of(1, 1), WALL);
        maze.setCell(Point.of(1, 2), WALL);
        maze.setCell(Point.of(1, 3), WALL);
        maze.setCell(Point.of(1, 4), good);
        maze.setCell(Point.of(2, 0), good);
        maze.setCell(Point.of(2, 1), bad);
        maze.setCell(Point.of(2, 2), bad);
        maze.setCell(Point.of(2, 3), bad);
        maze.setCell(Point.of(2, 4), good);
        maze.setCell(Point.of(3, 0), good);
        maze.setCell(Point.of(3, 1), WALL);
        maze.setCell(Point.of(3, 2), WALL);
        maze.setCell(Point.of(3, 3), WALL);
        maze.setCell(Point.of(3, 4), good);
        maze.setCell(Point.of(4, 0), good);
        maze.setCell(Point.of(4, 1), good);
        maze.setCell(Point.of(4, 2), good);
        maze.setCell(Point.of(4, 3), good);
        maze.setCell(Point.of(4, 4), good);
        gameSettings = MutableGameSettings.builder()
            .mazeHeight(5)
            .mazeWidth(5)
            .end(Point.of(0, 4))
            .pathRenderSpeedMs(0)
            .additionalTypes(List.of(good, bad))
            .build();
        printStream = mock(PrintStream.class);
        doNothing().when(printStream).println(anyChar());
        doNothing().when(printStream).print(anyChar());
        renderer = new DefaultMazeRenderer(gameSettings.immutable(), printStream);
    }

    @Test
    void noCost() {
        BfsSolver bfsSolver = new BfsSolver(gameSettings.immutable());
        var pathBfs = bfsSolver.solve(maze);
        DfsSolver dfsSolver = new DfsSolver(gameSettings.immutable());
        var pathDfs = dfsSolver.solve(maze);

        assertTrue(pathBfs.size() <= pathDfs.size());
        assertEquals(5, pathBfs.size());
        assertTrue(pathBfs.contains(Point.of(0, 1)));
        assertTrue(pathBfs.contains(Point.of(0, 2)));
        assertTrue(pathBfs.contains(Point.of(0, 3)));
        renderer.render(maze, pathBfs);
        renderer.render(maze, pathDfs);
    }

    @Test
    void noPath() {
        Maze noPath = new Maze(2, 2);
        noPath.setCell(Point.of(0, 0), WALL);
        noPath.setCell(Point.of(0, 1), WALL);
        noPath.setCell(Point.of(1, 0), WALL);
        noPath.setCell(Point.of(1, 1), WALL);
        gameSettings.end(Point.of(1, 1));
        gameSettings.mazeWidth(2);
        gameSettings.mazeHeight(2);
        BfsSolver bfsSolver = new BfsSolver(gameSettings.immutable());
        DfsSolver dfsSolver = new DfsSolver(gameSettings.immutable());
        BellmanSolver bellmanSolver = new BellmanSolver(gameSettings.immutable());
        SpfaSolver spfaSolver = new SpfaSolver(gameSettings.immutable());
        AStarSolver aStarSolver = new AStarSolver(gameSettings.immutable());

        assertThrows(PathNotFoundException.class, () -> bfsSolver.solve(noPath));
        assertThrows(PathNotFoundException.class, () -> dfsSolver.solve(noPath));
        assertThrows(PathNotFoundException.class, () -> bellmanSolver.solve(noPath));
        assertThrows(PathNotFoundException.class, () -> spfaSolver.solve(noPath));
        assertThrows(PathNotFoundException.class, () -> aStarSolver.solve(noPath));
    }

    @Test
    void hasCost() {
        BellmanSolver bellmanSolver = new BellmanSolver(gameSettings.immutable());
        var bellmanPath = bellmanSolver.solve(maze);
        SpfaSolver spfaSolver = new SpfaSolver(gameSettings.immutable());
        var spfaPath = spfaSolver.solve(maze);
        AStarSolver aStarSolver = new AStarSolver(gameSettings.immutable());
        var aStarPath = aStarSolver.solve(maze);

        renderer.render(maze, aStarPath);
        renderer.render(maze, bellmanPath);
        renderer.render(maze, spfaPath);
        assertEquals(aStarPath, bellmanPath);
        assertEquals(bellmanPath, spfaPath);
        assertFalse(aStarPath.contains(Point.of(0, 1)));
        assertFalse(aStarPath.contains(Point.of(0, 2)));
        assertFalse(aStarPath.contains(Point.of(0, 3)));
        assertEquals(13, bellmanPath.size());
    }

    @Test
    void randomized() {
        for (int i = 0; i < 100; i++) {
            gameSettings.mazeWidth(20);
            gameSettings.mazeHeight(20);
            gameSettings.biomesFreq(2.0);
            gameSettings.end(Point.of(getRandomInt(19), getRandomInt(19)));
            gameSettings.additionalTypes(DEFAULT_SETTINGS.additionalTypes());
            Maze maze =
                new PrimGenerator(gameSettings.immutable(),
                    new PrimitiveBiomeGenerator(gameSettings.immutable())).generate();
            renderer.render(maze);

            BfsSolver bfsSolver = new BfsSolver(gameSettings.immutable());
            var pathBfs = bfsSolver.solve(maze);
            DfsSolver dfsSolver = new DfsSolver(gameSettings.immutable());
            var pathDfs = dfsSolver.solve(maze);
            BellmanSolver bellmanSolver = new BellmanSolver(gameSettings.immutable());
            var bellmanPath = bellmanSolver.solve(maze);
            SpfaSolver spfaSolver = new SpfaSolver(gameSettings.immutable());
            var spfaPath = spfaSolver.solve(maze);
            AStarSolver aStarSolver = new AStarSolver(gameSettings.immutable());
            var aStarPath = aStarSolver.solve(maze);

            assertFalse(pathBfs.isEmpty());
            assertTrue(pathBfs.contains(gameSettings.start()));
            assertTrue(pathBfs.contains(gameSettings.end()));
            assertTrue(pathBfs.size() >= gameSettings.end().row() + gameSettings.end().col() - 2);

            assertFalse(pathDfs.isEmpty());
            assertTrue(pathDfs.contains(gameSettings.start()));
            assertTrue(pathDfs.contains(gameSettings.end()));
            assertTrue(pathDfs.size() >= gameSettings.end().row() + gameSettings.end().col() - 2);

            assertFalse(bellmanPath.isEmpty());
            assertTrue(bellmanPath.contains(gameSettings.start()));
            assertTrue(bellmanPath.contains(gameSettings.end()));
            assertTrue(bellmanPath.size() >= gameSettings.end().row() + gameSettings.end().col() - 2);

            assertFalse(spfaPath.isEmpty());
            assertTrue(spfaPath.contains(gameSettings.start()));
            assertTrue(spfaPath.contains(gameSettings.end()));
            assertTrue(spfaPath.size() >= gameSettings.end().row() + gameSettings.end().col() - 2);

            assertFalse(aStarPath.isEmpty());
            assertTrue(aStarPath.contains(gameSettings.start()));
            assertTrue(aStarPath.contains(gameSettings.end()));
            assertTrue(aStarPath.size() >= gameSettings.end().row() + gameSettings.end().col() - 2);
        }
    }

    @Test
    void spfaAdditionalTest() {
        gameSettings = MutableGameSettings.builder()
            .mazeHeight(3)
            .mazeWidth(3)
            .end(Point.of(2, 2))
            .pathRenderSpeedMs(0)
            .build();
        renderer = new DefaultMazeRenderer(gameSettings.immutable(), printStream);
        var best = new CellType(true, -1, 's');
        var good = new CellType(true, 1, 'g');
        var bad = new CellType(true, 20, 'b');
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
