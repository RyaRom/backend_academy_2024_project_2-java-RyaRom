package backend.academy.service.impl;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.exception.IncorrectSettingsException;
import backend.academy.service.Generator;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import static backend.academy.data.maze.CellType.PASSAGE;
import static backend.academy.data.maze.CellType.WALL;
import static backend.academy.utils.Randomizer.pullRandomObject;

@RequiredArgsConstructor
public class MazeGenerator implements Generator {
    private final GameSettings gameSettings;

    /**
     * Generates a maze based on the game settings
     *
     * @return the generated maze
     */
    @Override
    public Maze generate() {
        validateSettings();
        return switch (gameSettings.generationAlgorithm()) {
            case PRIM -> generatePrim();
        };
    }

    /**
     * Validates the game settings
     */
    private void validateSettings() {
        Point start = gameSettings.start();
        Point end = gameSettings.end();
        if (gameSettings.mazeHeight() < 1
            || gameSettings.mazeWidth() < 1
            || start.col() < 0 || start.row() < 0
            || end.col() < 0 || end.row() < 0
            || start.col() >= gameSettings.mazeWidth()
            || start.row() >= gameSettings.mazeHeight()
            || end.col() >= gameSettings.mazeWidth()
            || end.row() >= gameSettings.mazeHeight()
            || start.equals(end)) {
            throw new IncorrectSettingsException();
        }
    }

    /**
     * Checks if a cell has exactly one neighbor passage
     *
     * @param maze  the maze
     * @param point the cell
     * @return true if the cell has exactly one neighbor passage, false otherwise
     */
    private boolean checkCellNeighbors(Maze maze, Point point) {
        return point.getNeighbours(maze).stream()
            .map(maze::getCell)
            .filter(c -> c.type() == PASSAGE)
            .count() == 1;
    }

    /**
     * Checks if a cell has any neighbors passage
     *
     * @param maze  the maze
     * @param point the cell
     * @return true if the cell has any neighbor passage, false otherwise
     */
    private boolean checkCellHasNeighbors(Maze maze, Point point) {
        return point.getNeighbours(maze).stream()
            .map(maze::getCell)
            .anyMatch(c -> c.type() == PASSAGE);
    }

    /**
     * Add neighbors to the toVisit list
     *
     * @param toVisit the list to add neighbors to
     * @param current the current cell
     * @param maze    the maze
     */
    private void addNeighbors(List<Point> toVisit, Point current, Maze maze) {
        current.getNeighbours(maze).stream()
            .filter(n -> maze.getCell(n).type() == WALL)
            .forEach(toVisit::add);
    }

    /**
     * Make a point reachable from the start
     *
     * @param maze  the maze
     * @param point the point to make reachable
     */
    private void makePointReachable(Maze maze, Point point) {
        maze.setPoint(point, PASSAGE);
        List<Point> neighbours = point.getNeighbours(maze);
        Point current = point;
        while (!checkCellHasNeighbors(maze, current)) {
            current = pullRandomObject(neighbours);
            maze.setPoint(current, PASSAGE);
            addNeighbors(neighbours, current, maze);
        }
    }

    /**
     * Generates a maze using the Prim algorithm
     *
     * @return the generated maze
     */
    private Maze generatePrim() {
        Maze maze = new Maze(
            gameSettings.mazeHeight(),
            gameSettings.mazeWidth()
        );

        Point start = gameSettings.start();
        Point end = gameSettings.end();
        maze.setPoint(start, PASSAGE);
        List<Point> toVisit = new ArrayList<>();
        addNeighbors(toVisit, start, maze);

        while (!toVisit.isEmpty()) {
            Point current = pullRandomObject(toVisit);
            if (!checkCellNeighbors(maze, current)) {
                continue;
            }

            maze.setPoint(current, PASSAGE);
            addNeighbors(toVisit, current, maze);
        }
        makePointReachable(maze, end);

        return maze;
    }
}
