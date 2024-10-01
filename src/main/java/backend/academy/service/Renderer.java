package backend.academy.service;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import java.util.Queue;

public interface Renderer {
    void render(Maze maze);

    void render(Maze maze, Queue<Point> path);
}