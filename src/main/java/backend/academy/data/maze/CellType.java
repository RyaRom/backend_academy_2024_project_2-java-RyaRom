package backend.academy.data.maze;

/**
 * Class representing type of the cell
 *
 * <p>
 * Behaviour of cells with cost < 0 is undefined
 * </p>
 *
 * @param isPassage true if the cell is a passage, false if it is a wall
 * @param cost      the cost to move through this cell
 * @param render    the character to render this cell as
 */
public record CellType(boolean isPassage, int cost, char render) {
    public static final CellType PASSAGE = new CellType(true, 5, '░');

    public static final CellType WALL = new CellType(false, Integer.MAX_VALUE, '█');
}
