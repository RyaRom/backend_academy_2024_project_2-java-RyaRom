package backend.academy.data.maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

/**
 * Class representing the point coordinates
 */
public record Point(int row, int col) {
    public static Point of(int row, int col) {
        return new Point(row, col);
    }

    /**
     * Returns the upper neighbour of the point
     *
     * @return the upper neighbour or null if the point is on the top row
     */
    @Nullable
    public Point upper() {
        if (row == 0) {
            return null;
        }
        return new Point(row - 1, col);
    }

    /**
     * Returns the lower neighbour of the point
     *
     * @param maxHeight the maximum height of the maze
     * @return the lower neighbour or null if the point is on the bottom row
     */
    @Nullable
    public Point lower(int maxHeight) {
        if (row == maxHeight - 1) {
            return null;
        }
        return new Point(row + 1, col);
    }

    /**
     * Returns the left neighbour of the point
     *
     * @return the left neighbour or null if the point is on the leftmost column
     */
    @Nullable
    public Point left() {
        if (col == 0) {
            return null;
        }
        return new Point(row, col - 1);
    }

    /**
     * Returns the right neighbour of the point
     *
     * @param maxWidth the maximum width of the maze
     * @return the right neighbour or null if the point is on the rightmost column
     */
    @Nullable
    public Point right(int maxWidth) {
        if (col == maxWidth - 1) {
            return null;
        }
        return new Point(row, col + 1);
    }

    /**
     * Returns a list of all neighbours of the point
     *
     * @param maze the maze
     * @return the list of neighbours
     */
    public List<Point> getNeighbours(Maze maze) {
        List<Point> neighbours = new ArrayList<>();
        Optional.ofNullable(this.upper())
            .ifPresent(neighbours::add);
        Optional.ofNullable(this.lower(maze.height()))
            .ifPresent(neighbours::add);
        Optional.ofNullable(this.left())
            .ifPresent(neighbours::add);
        Optional.ofNullable(this.right(maze.width()))
            .ifPresent(neighbours::add);
        return neighbours;
    }

    /**
     * Returns a list of all passage neighbours of the point
     *
     * @param maze the maze
     * @return the list of passage neighbours
     */
    public List<Point> getPassageNeighbours(Maze maze) {
        return this.getNeighbours(maze).stream()
            .filter(p -> maze.getCell(p).type().isPassage())
            .toList();
    }

    public <T> void setInArray(T[][] array, T val) {
        array[row][col] = val;
    }

    public <T> T getFromArray(T[][] array) {
        return array[row][col];
    }
}
