package backend.academy.service.solvers;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.service.Solver;
import java.util.Queue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BellmanSolver implements Solver {
    private final GameSettings gameSettings;

    @Override
    public Queue<Point> solve(Maze maze) {
        return null;
    }
}