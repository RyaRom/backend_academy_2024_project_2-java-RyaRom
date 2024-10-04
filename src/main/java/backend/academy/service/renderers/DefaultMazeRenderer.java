package backend.academy.service.renderers;

import backend.academy.data.settings.GameSettings;
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
                outputWriter.print(getRenderForCell(cell));
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
                    outputWriter.print(getRenderForCell(cell));
                }
            }
            outputWriter.println();
        }
        outputWriter.println();
    }

    @SuppressWarnings("ReturnCount")
    private char getRenderForCell(Cell cell) {
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
            return gameSettings.wallRender();
        }
        return type.render();
    }

}
