package backend.academy.service.generators;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import static backend.academy.utils.Randomizer.imperfectionRandom;
import static backend.academy.utils.Randomizer.pullRandomObject;

@RequiredArgsConstructor
public class PrimGenerator implements Generator {
    private final GameSettings gameSettings;

    private final BiomeGenerator biomeGenerator;

    /**
     * Generates a maze using the Prim algorithm
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
        List<Point> toVisit = new ArrayList<>(start.getWallNeighbours(maze));

        while (!toVisit.isEmpty()) {
            Point current = pullRandomObject(toVisit);
            if (maze.getNeighbourPassages(current) != 1) {
                continue;
            }

            maze.setCellBiomeType(current, biomes);
            toVisit.addAll(current.getWallNeighbours(maze));
        }
        maze.makePointReachable(end, start, biomes);
        imperfectionRandom((int) (gameSettings.biomesFreq() * 10), maze, biomes);

        return maze;
    }

}
