package backend.academy.data.maze;

/**
 * Class representing the cell of labyrinth
 *
 * @param coordinates the coordinates of the cell in the maze
 * @param type       the parameters of the cell {@link CellType}
 */
public record Cell(Point coordinates, CellType type) {

}
