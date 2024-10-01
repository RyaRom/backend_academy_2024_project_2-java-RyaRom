package backend.academy.data.maze;

public record CellType(long id, boolean isPassage, int cost, char render) {
}
