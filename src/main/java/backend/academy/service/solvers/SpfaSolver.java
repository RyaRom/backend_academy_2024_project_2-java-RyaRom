package backend.academy.service.solvers;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;
import lombok.RequiredArgsConstructor;
import static backend.academy.service.solvers.BellmanSolver.backtracking;

@RequiredArgsConstructor
public class SpfaSolver implements Solver {
    private final GameSettings gameSettings;

    private Integer[][] distances;

    @Override
    public List<Point> solve(Maze maze) {
        init();
        gameSettings.start().setInArray(distances, 0);
        PriorityBlockingQueue<Point> toVisit = new PriorityBlockingQueue<>(
            maze.width() * maze.height(),
            Comparator.comparingInt(p -> p.getFromArray(distances))
        );
        Map<Point, Integer> relaxed = new HashMap<>();
        toVisit.add(gameSettings.start());

        while (!toVisit.isEmpty()) {
            Point current = toVisit.poll();
            relaxed.putIfAbsent(current, 0);
            if (relaxed.get(current) > gameSettings.mazeWidth() * gameSettings.mazeHeight()) {
                continue;
            }
            relaxed.put(current, relaxed.get(current) + 1);
            relax(current, maze, toVisit);
        }
        return backtrack(maze);
    }

    private void init() {
        int height = gameSettings.mazeHeight();
        int width = gameSettings.mazeWidth();
        distances = new Integer[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                distances[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    private void relax(Point point, Maze maze, Queue<Point> toVisit) {
        for (var neighbour : point.getPassageNeighbours(maze)) {
            int newCost = point.getFromArray(distances) + maze.getCell(neighbour).type().cost();
            int oldCost = neighbour.getFromArray(distances);
            if (newCost < oldCost) {
                neighbour.setInArray(distances, newCost);
                toVisit.add(neighbour);
            }
        }
    }

    private List<Point> backtrack(Maze maze) {
        return backtracking(maze, gameSettings, distances);
    }
}
