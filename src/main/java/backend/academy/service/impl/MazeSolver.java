package backend.academy.service.impl;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.exception.PathNotFoundException;
import backend.academy.service.Solver;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    /**
     * Finds the path from start to end using depth-first search algorithm.
     * Path is not guaranteed to be the shortest or fastest
     *
     * @param maze the maze to solve
     * @return the path from start to end
     * @throws PathNotFoundException if no path is found
     */
    private Queue<Point> dfs(Maze maze) {
        return Optional.ofNullable(dfsRec(
                new State(gameSettings.start(), new ArrayDeque<>()),
                maze, new HashSet<>()))
            .orElseThrow(PathNotFoundException::new);
    }

    private Queue<Point> dfsRec(State current, Maze maze, Set<Point> visited) {
        if (visited.contains(current.point)) {
            return null;
        }

        visited.add(current.point);
        current.path.add(current.point);

        if (current.point.equals(gameSettings.end())) {
            return current.path;
        }

        for (var neighbour : getPassageNeighbours(current.point, maze)) {
            var path = dfsRec(new State(neighbour, new ArrayDeque<>(current.path)), maze, visited);
            if (path != null) {
                return path;
            }
        }
        return null;
    }

    /**
     * Finds the path from start to end using breadth-first search algorithm.
     * Path is guaranteed to be the shortest but not guaranteed to be the fastest
     *
     * @param maze the maze to solve
     * @return the path from start to end
     * @throws PathNotFoundException if no path is found
     */

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
            for (Point point : getPassageNeighbours(current.point, maze)) {
                if (!visited.contains(point)) {
                    toVisit.add(new State(point, new ArrayDeque<>(current.path)));
                }
            }
        }
        throw new PathNotFoundException();
    }

    private List<Point> getPassageNeighbours(Point point, Maze maze) {
        return point.getNeighbours(maze).stream()
            .filter(p -> maze.getCell(p).type() == PASSAGE)
            .toList();
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
