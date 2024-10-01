package backend.academy.service.impl;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.service.Solver;
import lombok.RequiredArgsConstructor;
import java.util.Queue;

@RequiredArgsConstructor
public class MazeSolver implements Solver {
    private final GameSettings gameSettings;

    @Override
    public Queue<Point> solve(Maze maze) {
        return null;
    }
}
