package backend.academy.service.impl;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Cell;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.service.Renderer;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MazeRenderer implements Renderer {
    private final GameSettings gameSettings;

    @Override
    public void render(Maze maze) {
        var grid = maze.grid();
        for (Cell[] cells : grid) {
            for (Cell cell : cells) {
                System.out.print(cell.type().render());
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public void render(Maze maze, Queue<Point> path) {
        Set<Point> pathPoints = new HashSet<>();
        try (ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor()) {
            executor.scheduleAtFixedRate(() -> {
                    if (path.isEmpty()) {
                        executor.shutdown();
                    } else {
                        pathPoints.add(path.poll());
                        renderWithPath(maze, pathPoints);
                    }
                },
                0, gameSettings.pathRenderSpeedMs(), TimeUnit.MILLISECONDS);
        }
    }

    private void renderWithPath(Maze maze, Set<Point> path) {
        var grid = maze.grid();
        for (Cell[] cells : grid) {
            for (Cell cell : cells) {
                if (path.contains(cell.coordinates())) {
                    System.out.print(gameSettings.pathRender());
                } else {
                    System.out.print(cell.type().render());
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
