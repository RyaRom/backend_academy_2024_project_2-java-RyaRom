package backend.academy.data.maze;

import static backend.academy.data.maze.CellType.WALL;

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
}
