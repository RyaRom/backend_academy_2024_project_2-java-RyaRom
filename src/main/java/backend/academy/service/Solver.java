package backend.academy.service;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.exception.PathNotFoundException;
import java.util.Queue;

public interface Solver {
    /**
     * Solves the given maze and returns the path from start to end.
     * Takes parameters from {@link GameSettings}
     *
     * @param maze the maze to solve
     * @return the path from start to end
     * @throws PathNotFoundException if the maze doesn't contain path from start to end
     */
    Queue<Point> solve(Maze maze);
}
