package backend.academy.data.maze;

public record CellType(long id, boolean isPassage, int cost, char render) {
    public static final CellType PASSAGE = new CellType(0, true, 1, '░');

    public static final CellType WALL = new CellType(1, false, Integer.MAX_VALUE, '█');
}
