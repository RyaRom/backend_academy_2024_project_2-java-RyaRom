package backend.academy.service;

import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import java.util.Queue;

public interface Solver {
    Queue<Point> solve(Maze maze);
}
