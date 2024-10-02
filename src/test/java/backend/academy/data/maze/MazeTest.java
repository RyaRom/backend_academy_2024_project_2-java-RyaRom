package backend.academy.data.maze;

import backend.academy.data.GameSettings;
import backend.academy.service.renderers.DefaultMazeRenderer;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import static backend.academy.data.maze.CellType.PASSAGE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MazeTest {
    private GameSettings gameSettings = GameSettings.builder().build();

    private Maze maze;

    private DefaultMazeRenderer renderer = new DefaultMazeRenderer(gameSettings, System.out);

    @Test
    void makePointReachable() {
        maze = new Maze(5, 5);
        Point end = Point.of(4, 4);
        maze.setCell(Point.of(2, 2), PASSAGE);
        renderer.render(maze);

        assertFalse(maze.checkIfReachable(end, gameSettings.start(), new HashSet<>()));

        maze.makePointReachable(end, gameSettings.start());

        assertTrue(maze.checkIfReachable(end, gameSettings.start(), new HashSet<>()));

        renderer.render(maze);
    }
}
