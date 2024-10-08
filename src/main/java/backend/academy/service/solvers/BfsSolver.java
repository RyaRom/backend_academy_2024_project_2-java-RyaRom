package backend.academy.service.solvers;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.exception.PathNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BfsSolver implements Solver {
    private final GameSettings gameSettings;

    /**
     * Finds the path from start to end using breadth-first search algorithm.
     * Path is guaranteed to be the shortest but not guaranteed to be the fastest
     *
     * @param maze the maze to solve
     * @return the path from start to end
     * @throws PathNotFoundException if no path is found
     */
    @Override
    public List<Point> solve(Maze maze) {
        Queue<State> toVisit = new ArrayDeque<>();
        Set<Point> visited = new HashSet<>();
        toVisit.add(new State(gameSettings.start(), new ArrayList<>()));

        while (!toVisit.isEmpty()) {
            State current = toVisit.poll();
            Point point = current.point;

            current.path.add(point);
            visited.add(point);

            if (point.equals(gameSettings.end())) {
                return current.path;
            }

            for (Point p : point.getPassageNeighbours(maze)) {
                if (!visited.contains(p)) {
                    toVisit.add(new State(p, new ArrayList<>(current.path)));
                }
            }
        }
        throw new PathNotFoundException();
    }

    private record State(
        Point point,
        List<Point> path
    ) {
    }
}
