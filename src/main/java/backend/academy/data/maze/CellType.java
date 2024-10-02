package backend.academy.data.maze;

/**
 * Class representing type of the cell
 *
 * @param id        id of the cell type
 * @param isPassage true if the cell is a passage, false if it is a wall
 * @param cost      the cost to move through this cell
 * @param render    the character to render this cell as
 */
public record CellType(long id, boolean isPassage, int cost, char render) {
    public static final CellType PASSAGE = new CellType(0, true, 1, '░');

    public static final CellType WALL = new CellType(1, false, Integer.MAX_VALUE, '█');
}
