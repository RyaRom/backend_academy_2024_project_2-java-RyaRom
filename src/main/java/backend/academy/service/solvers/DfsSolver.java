package backend.academy.service.solvers;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.exception.PathNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DfsSolver implements Solver {
    private final GameSettings gameSettings;

    private Set<Point> visited;

    private Maze maze;

    /**
     * Finds the path from start to end using depth-first search algorithm.
     * Path is not guaranteed to be the shortest or fastest
     *
     * @param maze the maze to solve
     * @return the path from start to end
     * @throws PathNotFoundException if no path is found
     */
    @Override
    public List<Point> solve(Maze maze) {
        this.maze = maze;
        visited = new HashSet<>();
        return Optional.ofNullable(dfsRec(
                new State(
                    gameSettings.start(),
                    new ArrayList<>())
            ))
            .orElseThrow(PathNotFoundException::new);
    }

    private List<Point> dfsRec(State current) {
        Point point = current.point;
        if (visited.contains(point)) {
            return null;
        }

        visited.add(point);
        current.path.add(point);

        if (point.equals(gameSettings.end())) {
            return current.path;
        }

        for (var neighbour : point.getPassageNeighbours(maze)) {
            var path = dfsRec(new State(neighbour, new ArrayList<>(current.path)));
            if (path != null) {
                return path;
            }
        }
        return null;
    }

    private record State(
        Point point,
        List<Point> path
    ) {
    }
}
