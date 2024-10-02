package backend.academy.data.maze;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static backend.academy.data.maze.CellType.PASSAGE;
import static backend.academy.data.maze.CellType.WALL;
import static backend.academy.utils.Randomizer.pullRandomObject;

/**
 * Class representing the maze
 *
 * @param height height of the maze
 * @param width  width of the maze
 * @param grid   2D array of cells representing the maze
 */
public record Maze(int height, int width, Cell[][] grid) {
    public Maze(int height, int width) {
        this(height, width, initGrid(height, width));
    }

    /**
     * Initialize the grid with walls
     *
     * @param height height of the maze
     * @param width  width of the maze
     * @return 2D array of cells representing the maze
     */
    private static Cell[][] initGrid(int height, int width) {
        var maze = new Cell[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = new Cell(Point.of(i, j), WALL);
            }
        }
        return maze;
    }

    /**
     * Get the cell at the given point
     *
     * @param point the point to get the cell from
     * @return the cell at the given point
     */
    public Cell getCell(Point point) {
        return grid[point.row()][point.col()];
    }

    /**
     * Set the cell at the given point
     *
     * @param point the point to set the cell at
     * @param type  the type of the cell
     */
    public void setCell(Point point, CellType type) {
        grid[point.row()][point.col()] = new Cell(point, type);
    }

    /**
     * Make a point reachable from the start point
     *
     * @param point the point to make reachable
     * @param start the start point
     */
    public void makePointReachable(Point point, Point start) {
        setCell(point, PASSAGE);
        List<Point> neighbours = point.getNeighbours(this);
        Point current = point;
        while (!checkIfReachable(current, start, new HashSet<>())) {
            current = pullRandomObject(neighbours);
            setCell(current, PASSAGE);
            addNeighbours(neighbours, current);
        }
    }

    /**
     * Check if a point is reachable from the start point using dfs
     *
     * @param current the point to check
     * @param start   the start point
     * @return true if the point is reachable, false otherwise
     */
    public boolean checkIfReachable(Point current, Point start, Set<Point> visited) {
        if (start.equals(current)) {
            return true;
        }
        visited.add(current);

        return current.getNeighbours(this).stream()
            .filter(n -> getCell(n).type().isPassage()
                && !visited.contains(n))
            .map(n -> checkIfReachable(n, start, visited))
            .reduce((a, b) -> a || b).orElse(false);
    }

    /**
     * Add neighbours to the toVisit list
     *
     * @param toVisit the list to add neighbours to
     * @param current the current cell
     */
    public void addNeighbours(List<Point> toVisit, Point current) {
        current.getNeighbours(this).stream()
            .filter(n -> !this.getCell(n).type().isPassage())
            .forEach(toVisit::add);
    }

    /**
     * Checks if a cell has exactly one neighbour passage
     *
     * @param point the cell
     * @return true if the cell has exactly one neighbour passage, false otherwise
     */
    public boolean checkCellNeighbour(Point point) {
        return point.getNeighbours(this).stream()
            .map(this::getCell)
            .filter(c -> c.type().isPassage())
            .count() == 1;
    }

}
