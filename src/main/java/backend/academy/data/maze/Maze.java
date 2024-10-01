package backend.academy.data.maze;

/**
 * Class representing the maze
 * @param height height of the maze
 * @param width  width of the maze
 * @param grid   2D array of cells representing the maze
 */
public record Maze(int height, int width, Cell[][] grid) {
    public Maze(int height, int width) {
        this(height, width, new Cell[height][width]);
    }
}
