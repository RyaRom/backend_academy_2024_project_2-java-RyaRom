package backend.academy.service.impl;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Cell;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.service.Renderer;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class MazeRenderer implements Renderer {
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
            clearScreen();
        }
    }

    @Override
    public void renderMenu(List<String> options, String menuName) {
        if (menuName == null || options == null || options.isEmpty()) {
            throw new IllegalArgumentException("No string from the empty array");
        }

        StringBuilder stringBuilder = new StringBuilder()
            .append("Select ")
            .append(menuName)
            .append(":")
            .append(System.lineSeparator())
            .append("0. Go back")
            .append(System.lineSeparator());

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i) == null) {
                continue;
            }
            stringBuilder.append(i + 1)
                .append(". ")
                .append(options.get(i))
                .append(System.lineSeparator());
        }
        stringBuilder.append("Enter your choice: ");
        outputWriter.println(stringBuilder);
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

    @SuppressWarnings("MagicNumber")
    private void clearScreen() {
        //for IDE's terminal
        for (int i = 0; i < 25; i++) {
            outputWriter.println();
        }

        outputWriter.print("\033[H\033[2J");
        outputWriter.flush();
    }
}
