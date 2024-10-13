package backend.academy.service.solvers;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.maze.Cell;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.exception.PathNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import static backend.academy.service.solvers.BellmanSolver.backtracking;

@Log4j2
@RequiredArgsConstructor
public class AStarSolver implements Solver {

    private final GameSettings gameSettings;

    private Integer[][] heuristic;

    private Integer[][] distances;

    private final Comparator<Point> aStarComparator =
        Comparator.comparingInt(point -> point.getFromArray(distances)
            + point.getFromArray(heuristic));

    private Set<Point> closed;

    private PriorityBlockingQueue<Point> toVisit;

    @Override
    public List<Point> solve(Maze maze) {
        distances = new Integer[maze.height()][maze.width()];
        for (Integer[] distance : distances) {
            Arrays.fill(distance, Integer.MAX_VALUE / 2);
        }
        heuristic = calculateHeuristic(maze);
        closed = new HashSet<>();
        gameSettings.start().setInArray(distances, 0);
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
        return backtracking(maze, gameSettings, distances);
    }

    private int manhattanDist(Point point) {
        return Math.abs(point.row() - gameSettings.end().row())
            + Math.abs(point.col() - gameSettings.end().col());
    }

    private Integer[][] calculateHeuristic(Maze maze) {
        Integer[][] heuristicMap = new Integer[maze.height()][maze.width()];
        Cell[][] grid = maze.grid();
        for (int i = 0, gridLength = grid.length; i < gridLength; i++) {
            var row = grid[i];
            for (int j = 0, rowLength = row.length; j < rowLength; j++) {
                var cell = row[j];
                if (!cell.type().isPassage()) {
                    heuristicMap[i][j] = Integer.MAX_VALUE / 2;
                    continue;
                }
                heuristicMap[i][j] = manhattanDist(cell.coordinates()) + cell.type().cost();
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

            int newCost = point.getFromArray(distances) + maze.getCell(neighbour).type().cost();
            int oldCost = neighbour.getFromArray(distances);
            log.info("New cost: {}, old cost: {}", newCost, oldCost);
            if (newCost < oldCost) {
                log.info("Updating cost");
                neighbour.setInArray(distances, newCost);
                toVisit.add(neighbour);
            } else {
                log.info("Not updating cost");
            }
        }
    }
}
