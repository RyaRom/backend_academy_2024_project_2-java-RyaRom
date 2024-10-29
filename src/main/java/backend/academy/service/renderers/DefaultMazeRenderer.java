package backend.academy.service.renderers;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.maze.Cell;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import static backend.academy.data.maze.CellType.PASSAGE;
import static backend.academy.data.maze.CellType.WALL;

@RequiredArgsConstructor
public class DefaultMazeRenderer implements MazeRenderer {
    private final GameSettings gameSettings;

    private final PrintStream outputWriter;

    @Override
    public void render(Maze maze) {
        var grid = maze.grid();
        for (Cell[] cells : grid) {
            for (Cell cell : cells) {
                outputWriter.print(getRenderForCell(cell, maze));
            }
            outputWriter.println();
        }
        outputWriter.println();
    }

    @SneakyThrows @Override
    public void render(Maze maze, List<Point> path) {
        Set<Point> pathPoints = new HashSet<>();
        for (Point point : path) {
            pathPoints.add(point);
            renderWithPath(maze, pathPoints);
            Thread.sleep(gameSettings.pathRenderSpeedMs());
        }
    }

    private void renderWithPath(Maze maze, Set<Point> path) {
        var grid = maze.grid();
        for (Cell[] cells : grid) {
            for (Cell cell : cells) {
                if (path.contains(cell.coordinates())) {
                    outputWriter.print(gameSettings.pathRender());
                } else {
                    outputWriter.print(getRenderForCell(cell, maze));
                }
            }
            outputWriter.println();
        }
        outputWriter.println();
    }

    @SuppressWarnings("ReturnCount")
    private char getRenderForCell(Cell cell, Maze maze) {
        var type = cell.type();
        if (cell.coordinates().equals(gameSettings.start())) {
            return gameSettings.startRender();
        }
        if (cell.coordinates().equals(gameSettings.end())) {
            return gameSettings.endRender();
        }
        if (type.equals(PASSAGE)) {
            return gameSettings.passageRender();
        }
        if (type.equals(WALL)) {
            if (gameSettings.asciiMode()) {
                return getAsciiRender(cell, maze);
            }
            return gameSettings.wallRender();
        }
        return type.render();
    }

    private char getAsciiRender(Cell cell, Maze maze) {
        Point upper = cell.coordinates().upper();
        Point lower = cell.coordinates().lower(maze.height());
        Point right = cell.coordinates().right(maze.width());
        Point left = cell.coordinates().left();
        boolean isUpperWall = upper != null && !maze.getCell(upper).type().isPassage();
        boolean isLowerWall = lower != null && !maze.getCell(lower).type().isPassage();
        boolean isRightWall = right != null && !maze.getCell(right).type().isPassage();
        boolean isLeftWall = left != null && !maze.getCell(left).type().isPassage();
        return getAsciiSymbol(isUpperWall, isLowerWall, isRightWall, isLeftWall);
    }

    private char getAsciiSymbol(
        boolean isUpperWall,
        boolean isLowerWall,
        boolean isRightWall,
        boolean isLeftWall
    ) {
        if (isUpperWall && isLowerWall && isRightWall
            || isUpperWall && isLowerWall && isLeftWall) {
            return '+';
        }
        if ((isUpperWall || isLowerWall)) {
            return '|';
        }
        if (isRightWall || isLeftWall) {
            return '-';
        }
        return '+';
    }
}
