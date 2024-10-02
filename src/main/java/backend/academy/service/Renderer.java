package backend.academy.service;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import java.util.List;
import java.util.Queue;

public interface Renderer {
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
    void render(Maze maze, Queue<Point> path);

    /**
     * Renders the menu for CLI
     *
     * @param options  the options to render
     * @param menuName the name of the menu
     * @throws IllegalArgumentException  if the options are empty or null
     */

    void renderMenu(List<String> options, String menuName);
}
