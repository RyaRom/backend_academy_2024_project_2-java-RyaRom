package backend.academy.service.impl;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.exception.PathNotFoundException;
import backend.academy.service.Solver;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import static backend.academy.data.maze.CellType.PASSAGE;

@RequiredArgsConstructor
public class MazeSolver implements Solver {
    private final GameSettings gameSettings;

    @Override
    public Queue<Point> solve(Maze maze) {
        return switch (gameSettings.pathfindingAlgorithm()) {
            case BELLMAN -> bellman(maze);
            case A_STAR -> aStar(maze);
            case DFS -> dfs(maze);
            case BFS -> bfs(maze);
        };
    }

    private Queue<Point> dfs(Maze maze) {
        return null;
    }

    private Queue<Point> bfs(Maze maze) {
        Queue<State> toVisit = new ArrayDeque<>();
        Set<Point> visited = new HashSet<>();
        toVisit.add(new State(gameSettings.start(), new ArrayDeque<>()));
        while (!toVisit.isEmpty()) {
            State current = toVisit.poll();
            current.path.add(current.point);
            visited.add(current.point);
            if (current.point.equals(gameSettings.end())) {
                return current.path;
            }
            for (Point point : current.point.getNeighbours(maze)) {
                if (!visited.contains(point)
                    && maze.getCell(point).type() == PASSAGE) {
                    toVisit.add(new State(point, new ArrayDeque<>(current.path)));
                }
            }
        }
        throw new PathNotFoundException();
    }

    private Queue<Point> bellman(Maze maze) {
        return null;
    }

    private Queue<Point> aStar(Maze maze) {
        return null;
    }

    private record State(
        Point point,
        Queue<Point> path
    ) {
    }

}
