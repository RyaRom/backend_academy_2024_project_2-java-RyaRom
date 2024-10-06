package backend.academy.service.solvers;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.exception.PathNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class AStarSolver implements Solver {
    private final GameSettings gameSettings;

    private Map<Point, Integer> heuristic;

    private Map<Point, Integer> distances;

    private final Comparator<Point> aStarComparator =
        Comparator.comparingInt(point -> distances.getOrDefault(point, Integer.MAX_VALUE / 2)
            + heuristic.getOrDefault(point, Integer.MAX_VALUE / 2));

    private Set<Point> closed;

    private PriorityBlockingQueue<Point> toVisit;

    @Override
    public List<Point> solve(Maze maze) {
        heuristic = calculateHeuristic(maze);
        distances = new HashMap<>();
        closed = new HashSet<>();
        distances.put(gameSettings.start(), 0);
        toVisit = new PriorityBlockingQueue<>(maze.width() * maze.height(), aStarComparator);
        toVisit.add(gameSettings.start());
        log.info("Starting A-star\n\n\n");

        while (!toVisit.isEmpty()) {
            log.info("To visit: {}", toVisit);
            Point current = toVisit.poll();
            log.info("Current: {}", current);
            closed.add(current);
            assert current != null;
            if (current.equals(gameSettings.end())) {
                return backtrack(maze);
            }
            relax(current, maze);
        }
        throw new PathNotFoundException();
    }

    private List<Point> backtrack(Maze maze) {
        List<Point> path = new ArrayList<>();
        Point current = gameSettings.end();
        Set<Point> tracked = new HashSet<>();

        while (!current.equals(gameSettings.start())) {
            path.add(current);
            tracked.add(current);
            current = current.getPassageNeighbours(maze).stream()
                .filter(p -> !tracked.contains(p))
                .min(aStarComparator)
                .orElseThrow(PathNotFoundException::new);
        }
        path.add(gameSettings.start());
        return path.reversed();
    }

    private int manhattanDist(Point point) {
        return Math.abs(point.row() - gameSettings.end().row())
            + Math.abs(point.col() - gameSettings.end().col());
    }

    private Map<Point, Integer> calculateHeuristic(Maze maze) {
        Map<Point, Integer> heuristicMap = new HashMap<>();
        for (var row : maze.grid()) {
            for (var cell : row) {
                if (!cell.type().isPassage()) {
                    continue;
                }
                heuristicMap.put(
                    cell.coordinates(), manhattanDist(cell.coordinates())
                        + cell.type().cost()
                );
            }
        }
        return heuristicMap;
    }

    private void relax(Point point, Maze maze) {
        log.info("Relaxing {}", point);
        for (var neighbour : point.getPassageNeighbours(maze)) {
            log.info("Neighbour: {}", neighbour);
            if (closed.contains(neighbour)) {
                log.info("Already visited");
                continue;
            }

            int newCost = distances.get(point) + maze.getCell(neighbour).type().cost();
            int oldCost = distances.getOrDefault(neighbour, Integer.MAX_VALUE);
            log.info("New cost: {}, old cost: {}", newCost, oldCost);
            if (newCost < oldCost) {
                log.info("Updating cost");
                distances.put(neighbour, newCost);
                toVisit.add(neighbour);
            } else {
                log.info("Not updating cost");
            }
        }
    }
}
