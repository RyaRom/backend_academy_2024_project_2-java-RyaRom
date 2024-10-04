package backend.academy.service.renderers;

import backend.academy.data.settings.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import java.util.List;

public interface MazeRenderer {
    /**
     * Renders the maze. Takes rendering parameters from {@link GameSettings}
     *
     * @param maze the maze to render
     */
    void render(Maze maze);

    /**
     * Renders the maze with the path. Takes rendering parameters from {@link GameSettings}
     *
     * @param maze the maze to render
     * @param path the path to render
     */
    void render(Maze maze, List<Point> path);
}
