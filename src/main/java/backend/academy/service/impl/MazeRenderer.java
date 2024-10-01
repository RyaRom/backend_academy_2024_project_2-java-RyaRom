package backend.academy.service.impl;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.service.Renderer;
import java.util.Queue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MazeRenderer implements Renderer {
    private final GameSettings gameSettings;

    @Override
    public void render(Maze maze) {

    }

    @Override
    public void render(Maze maze, Queue<Point> path) {

    }
}
