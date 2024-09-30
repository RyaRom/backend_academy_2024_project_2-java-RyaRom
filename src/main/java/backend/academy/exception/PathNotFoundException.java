package backend.academy.exception;

public class PathNotFoundException extends RuntimeException {
    public PathNotFoundException() {
        super("Maze doesn't contain path from start to finish");
    }
}
