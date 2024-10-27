package backend.academy.service.generators;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.maze.Point;
import backend.academy.service.factories.GeneratorFactory;
import backend.academy.service.factories.SolverFactory;
import backend.academy.service.solvers.Solver;
import org.junit.jupiter.api.RepeatedTest;
import static backend.academy.data.enums.MazeGenerationAlgorithm.KRUSKAL;
import static backend.academy.data.enums.MazeGenerationAlgorithm.PRIM;
import static backend.academy.data.enums.PathfindingAlgorithm.BFS;
import static org.assertj.core.api.Assertions.assertWith;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MazeGeneratorsTest {
    private Solver solver;

    private Generator generator;

    private void setUp(GameSettings gameSettings) {
        generator = new GeneratorFactory(gameSettings).generator();
        solver = new SolverFactory(gameSettings).solver();
    }

    @RepeatedTest(100)
    void prim() {
        var gameSettings = GameSettings.builder()
            .mazeHeight(50)
            .mazeWidth(50)
            .start(Point.of(0, 0))
            .end(Point.of(49, 49))
            .pathfindingAlgorithm(BFS)
            .generationAlgorithm(PRIM)
            .build();
        setUp(gameSettings);

        assertWith(generator.generate(), maze -> {
            assertTrue(maze.isReachable(gameSettings.start(), gameSettings.end()));
            assertDoesNotThrow(() -> solver.solve(maze));
        });
    }

    @RepeatedTest(100)
    void kruskal() {
        var gameSettings = GameSettings.builder()
            .mazeHeight(50)
            .mazeWidth(50)
            .start(Point.of(0, 0))
            .end(Point.of(49, 49))
            .pathfindingAlgorithm(BFS)
            .generationAlgorithm(KRUSKAL)
            .build();
        setUp(gameSettings);

        assertWith(generator.generate(), maze -> {
            assertTrue(maze.isReachable(gameSettings.start(), gameSettings.end()));
            assertDoesNotThrow(() -> solver.solve(maze));
        });
    }
}
