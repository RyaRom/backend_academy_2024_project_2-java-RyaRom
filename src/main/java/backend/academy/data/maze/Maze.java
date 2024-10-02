package backend.academy.data.maze;

import java.util.List;
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
    public void setPoint(Point point, CellType type) {
        grid[point.row()][point.col()] = new Cell(point, type);
    }

    /**
     * Make a point reachable from the start
     *
     * @param point the point to make reachable
     */
    public void makePointReachable(Point point) {
        setPoint(point, PASSAGE);
        List<Point> neighbours = point.getNeighbours(this);
        Point current = point;
        while (!checkCellHasNeighbors(current)) {
            current = pullRandomObject(neighbours);
            setPoint(current, PASSAGE);
            addNeighbors(neighbours, current);
        }
    }

    /**
     * Checks if a cell has any neighbors passage
     *
     * @param point the cell
     * @return true if the cell has any neighbor passage, false otherwise
     */
    private boolean checkCellHasNeighbors(Point point) {
        return point.getNeighbours(this).stream()
            .map(this::getCell)
            .anyMatch(c -> c.type().isPassage());
    }

    /**
     * Add neighbors to the toVisit list
     *
     * @param toVisit the list to add neighbors to
     * @param current the current cell
     */
    public void addNeighbors(List<Point> toVisit, Point current) {
        current.getNeighbours(this).stream()
            .filter(n -> !this.getCell(n).type().isPassage())
            .forEach(toVisit::add);
    }

    /**
     * Checks if a cell has exactly one neighbor passage
     *
     * @param point the cell
     * @return true if the cell has exactly one neighbor passage, false otherwise
     */
    public boolean checkCellNeighbor(Point point) {
        return point.getNeighbours(this).stream()
            .map(this::getCell)
            .filter(c -> c.type().isPassage())
            .count() == 1;
    }

}
