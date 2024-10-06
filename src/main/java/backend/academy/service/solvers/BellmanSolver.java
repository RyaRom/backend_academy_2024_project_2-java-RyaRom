package backend.academy.service.solvers;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.exception.PathNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BellmanSolver implements Solver {
    private final GameSettings gameSettings;

    private Integer[][] distances;

    /**
     * Backtracking algorithm for finding the shortest path in a maze
     * for Bellman and SPFA algorithms
     *
     * @param maze         the maze
     * @param gameSettings the game settings
     * @param distances    the distances array
     * @return the shortest path
     */
    protected static List<Point> backtracking(Maze maze, GameSettings gameSettings, Integer[][] distances) {
        Point current = gameSettings.end();
        List<Point> path = new ArrayList<>();
        Set<Point> visited = new HashSet<>();

        while (!current.equals(gameSettings.start())) {
            path.add(current);
            visited.add(current);
            current = current.getPassageNeighbours(maze).stream()
                .filter(n -> !visited.contains(n))
                .min(Comparator.comparingInt(n -> n.getFromArray(distances)))
                .orElseThrow(PathNotFoundException::new);
        }
        path.add(gameSettings.start());

        Collections.reverse(path);
        return path;
    }

    @Override
    public List<Point> solve(Maze maze) {
        int height = gameSettings.mazeHeight();
        int width = gameSettings.mazeWidth();
        init();
        gameSettings.start().setInArray(distances, 0);

        for (int i = 0; i < height * width - 1; i++) {
            boolean updated = false;

            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    if (distances[j][k] == Integer.MAX_VALUE) {
                        continue;
                    }
                    Point point = new Point(j, k);
                    updated = updated || relax(point, maze);
                }
            }

            if (!updated) {
                break;
            }
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

    private boolean relax(Point point, Maze maze) {
        boolean updated = false;
        for (var neighbour : point.getPassageNeighbours(maze)) {
            int newCost = point.getFromArray(distances) + maze.getCell(neighbour).type().cost();
            int oldCost = neighbour.getFromArray(distances);
            if (newCost < oldCost) {
                neighbour.setInArray(distances, newCost);
                updated = true;
            }
        }
        return updated;
    }

    private List<Point> backtrack(Maze maze) {
        return backtracking(maze, gameSettings, distances);
    }
}
