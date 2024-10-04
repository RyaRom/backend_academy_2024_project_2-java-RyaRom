package backend.academy.service.solvers;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.service.Solver;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpfaSolver implements Solver {
    private final GameSettings gameSettings;

    private Integer[][] distances;

    @Override
    public Queue<Point> solve(Maze maze) {
        init();
        gameSettings.start().setInArray(distances, 0);
        Queue<Point> toVisit = new ArrayDeque<>();
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

    private Queue<Point> backtrack(Maze maze) {
        Point current = gameSettings.end();
        List<Point> path = new ArrayList<>();
        Set<Point> visited = new HashSet<>();

        while (!current.equals(gameSettings.start())) {
            path.add(current);
            visited.add(current);
            current = current.getNeighbours(maze).stream()
                .filter(n -> !visited.contains(n))
                .min(Comparator.comparingInt(n -> n.getFromArray(distances)))
                .orElse(gameSettings.start());
        }
        path.add(gameSettings.start());

        Collections.reverse(path);
        return new ArrayDeque<>(path);
    }
}
