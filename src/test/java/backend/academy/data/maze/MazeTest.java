package backend.academy.data.maze;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.service.generators.BiomeGenerator;
import backend.academy.service.generators.PrimitiveBiomeGenerator;
import backend.academy.service.renderers.DefaultMazeRenderer;
import backend.academy.service.renderers.MazeRenderer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static backend.academy.data.maze.CellType.PASSAGE;
import static backend.academy.data.maze.CellType.WALL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MazeTest {
    private final GameSettings gameSettings = GameSettings.builder().build();

    private final BiomeGenerator biomeGenerator = new PrimitiveBiomeGenerator(gameSettings);

    private Maze maze;

    @Test
    void makePointReachable() {
        Point start = Point.of(0, 0);
        Point end = Point.of(4, 4);
        maze = new Maze(5, 5);
        var biomes = biomeGenerator.generate();
        maze.setCellBiomeType(start, biomes);
        MazeRenderer renderer = new DefaultMazeRenderer(GameSettings.builder()
            .start(start)
            .end(end)
            .build(), System.out);
        renderer.render(maze);

        assertFalse(maze.isReachable(end, start));

        maze.makePointReachable(end, start, biomes);

        assertTrue(maze.isReachable(end, start));

        renderer.render(maze);
    }

    @Test
    void reachable() {
        maze = new Maze(4, 4);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                maze.grid()[i][j] = new Cell(Point.of(i, j), PASSAGE);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Assertions.assertTrue(maze.isReachable(Point.of(i, j), Point.of(j, i)));
            }
        }
    }

    @Test
    void notReachable() {
        maze = new Maze(2, 2);
        Point start = Point.of(0, 0);
        Point end = Point.of(1, 1);
        maze.setCell(Point.of(0, 0), PASSAGE);
        maze.setCell(Point.of(1, 0), PASSAGE);
        maze.setCell(Point.of(0, 1), PASSAGE);
        maze.setCell(Point.of(1, 1), WALL);
        assertFalse(maze.isReachable(end, start));
        maze.makePointReachable(end, start, new PrimitiveBiomeGenerator(gameSettings).generate());
        assertTrue(maze.isReachable(end, start));
    }
}
