package backend.academy.service.renderers;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Cell;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class DefaultMazeRenderer implements backend.academy.service.MazeRenderer {
    private final GameSettings gameSettings;

    private final PrintStream outputWriter;

    @Override
    public void render(Maze maze) {
        var grid = maze.grid();
        for (Cell[] cells : grid) {
            for (Cell cell : cells) {
                outputWriter.print(cell.type().render());
            }
            outputWriter.println();
        }
        outputWriter.println();
    }

    @SneakyThrows @Override
    public void render(Maze maze, Queue<Point> path) {
        Set<Point> pathPoints = new HashSet<>();
        while (!path.isEmpty()) {
            pathPoints.add(path.poll());
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
                    outputWriter.print(cell.type().render());
                }
            }
            outputWriter.println();
        }
        outputWriter.println();
    }

}
