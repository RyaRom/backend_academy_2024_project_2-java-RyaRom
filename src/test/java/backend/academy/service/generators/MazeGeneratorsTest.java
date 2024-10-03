package backend.academy.service.generators;

import backend.academy.data.MutableGameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.service.Generator;
import backend.academy.service.MazeRenderer;
import backend.academy.service.Solver;
import backend.academy.service.factories.GeneratorFactory;
import backend.academy.service.renderers.DefaultMazeRenderer;
import backend.academy.service.solvers.BfsSolver;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import static backend.academy.data.enums.MazeGenerationAlgorithm.PRIM;
import static backend.academy.data.enums.PathfindingAlgorithm.BFS;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MazeGeneratorsTest {

    private final MutableGameSettings gameSettings = MutableGameSettings.builder()
        .mazeHeight(50)
        .mazeWidth(50)
        .end(Point.of(49, 49))
        .start(Point.of(0, 0))
        .pathfindingAlgorithm(BFS)
        .build();

    private final Solver solver = new BfsSolver(gameSettings.immutable());

    private final MazeRenderer renderer = new DefaultMazeRenderer(gameSettings.immutable(), System.out);

    private Maze maze;

    private Generator generator;

    @Test
    void prim() {
        gameSettings.generationAlgorithm(PRIM);
        GeneratorFactory factory = new GeneratorFactory(gameSettings.immutable());
        generator = factory.generator();

        for (int i = 0; i < 100; i++) {
            maze = generator.generate();
            assertTrue(maze.checkIfReachable(gameSettings.start(), gameSettings.end(), new HashSet<>()));
            assertDoesNotThrow(() -> solver.solve(maze));
            if (i % 10 == 0) {
                renderer.render(maze);
            }
        }
    }
}
