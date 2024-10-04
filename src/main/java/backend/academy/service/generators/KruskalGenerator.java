package backend.academy.service.generators;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.service.BiomeGenerator;
import backend.academy.service.Generator;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import static backend.academy.utils.Randomizer.imperfectionRandom;
import static backend.academy.utils.Randomizer.pullRandomObject;

@RequiredArgsConstructor
public class KruskalGenerator implements Generator {
    private final GameSettings gameSettings;

    private final BiomeGenerator biomeGenerator;

    private List<Point> walls;

    private Integer[][] weights;

    /**
     * Generates a maze using the Kruskal's algorithm.
     *
     * @return the generated maze
     */
    @Override
    @SuppressWarnings("MagicNumber")
    public Maze generate() {
        Maze maze = new Maze(
            gameSettings.mazeHeight(),
            gameSettings.mazeWidth()
        );
        var biomes = biomeGenerator.generate();
        Point start = gameSettings.start();
        Point end = gameSettings.end();
        maze.setCellBiomeType(start, biomes);
        init();

        while (!walls.isEmpty()) {
            Point current = pullRandomObject(walls);
            var neighbours = current.getPassageNeighbours(maze);
            if (neighbours.isEmpty()) {
                maze.setCellBiomeType(current, biomes);
                continue;
            }
            if (neighbours.size() > 1 && neighbours
                .stream()
                .map(n -> n.getFromArray(weights))
                .distinct()
                .count() == 1) {
                continue;
            }

            final int newWeight = neighbours.getFirst().getFromArray(weights);
            maze.setCellBiomeType(current, biomes);
            current.setInArray(weights, newWeight);
            neighbours.forEach(n -> unionWeights(n, n.getFromArray(weights), newWeight, maze));
        }
        if (!maze.isReachable(end, start)) {
            maze.makePointReachable(end, start, biomes);
            maze.makePointReachable(start, end, biomes);
        }
        imperfectionRandom((int) (gameSettings.biomesFreq() * 10), maze, biomes);

        return maze;
    }

    private void init() {
        int height = gameSettings.mazeHeight();
        int width = gameSettings.mazeWidth();
        walls = new ArrayList<>();
        weights = new Integer[height][width];
        int w = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                weights[i][j] = w++;
                walls.add(new Point(i, j));
            }
        }
    }

    private void unionWeights(Point start, int from, int to, Maze maze) {
        if (start.getFromArray(weights) != from || from == to) {
            return;
        }
        start.setInArray(weights, to);
        start.getNeighbours(maze).forEach(n -> unionWeights(n, from, to, maze));
    }
}
