package backend.academy.service.generators;

import backend.academy.data.gameSettings.MutableGameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.service.renderers.MazeRenderer;
import backend.academy.service.solvers.Solver;
import backend.academy.service.factories.GeneratorFactory;
import backend.academy.service.factories.SolverFactory;
import backend.academy.service.renderers.DefaultMazeRenderer;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import static backend.academy.data.enums.MazeGenerationAlgorithm.KRUSKAL;
import static backend.academy.data.enums.MazeGenerationAlgorithm.PRIM;
import static backend.academy.data.enums.PathfindingAlgorithm.BFS;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MazeGeneratorsTest {

    private MutableGameSettings gameSettings = MutableGameSettings.builder()
        .mazeHeight(50)
        .mazeWidth(50)
        .end(Point.of(49, 49))
        .start(Point.of(0, 0))
        .pathfindingAlgorithm(BFS)
        .build();

    private final MazeRenderer renderer = new DefaultMazeRenderer(gameSettings.immutable(), System.out);

    private Maze maze;

    private GeneratorFactory generatorFactory;

    private SolverFactory solverFactory;

    private Solver solver;

    private Generator generator;

    @Test
    void prim() {
        gameSettings.generationAlgorithm(PRIM);
        generatorFactory = new GeneratorFactory(gameSettings.immutable());
        solverFactory = new SolverFactory(gameSettings.immutable());
        generator = generatorFactory.generator();
        solver = solverFactory.solver();

        for (int i = 0; i < 100; i++) {
            maze = generator.generate();
            assertTrue(maze.isReachable(gameSettings.start(), gameSettings.end()));
            assertDoesNotThrow(() -> solver.solve(maze));
            if (i % 10 == 0) {
                renderer.render(maze);
            }
        }
    }

    @Test
    void kruskal() {
        gameSettings.generationAlgorithm(KRUSKAL);
        gameSettings.mazeHeight(30);
        gameSettings.mazeWidth(30);
        gameSettings.end(Point.of(29, 29));
        gameSettings.additionalTypes(Collections.emptyList());

        generatorFactory = new GeneratorFactory(gameSettings.immutable());
        solverFactory = new SolverFactory(gameSettings.immutable());
        generator = generatorFactory.generator();
        solver = solverFactory.solver();

        for (int i = 0; i < 10; i++) {
            maze = generator.generate();
            renderer.render(maze);
            assertTrue(maze.isReachable(gameSettings.start(), gameSettings.end()));
            assertDoesNotThrow(() -> solver.solve(maze));
        }
    }
}
