package backend.academy.service.generators;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.service.Generator;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import static backend.academy.data.maze.CellType.PASSAGE;
import static backend.academy.utils.Randomizer.pullRandomObject;

@RequiredArgsConstructor
public class PrimGenerator implements Generator {
    private final GameSettings gameSettings;

    /**
     * Generates a maze using the Prim algorithm
     *
     * @return the generated maze
     */
    @Override
    public Maze generate() {
        Maze maze = new Maze(
            gameSettings.mazeHeight(),
            gameSettings.mazeWidth()
        );

        Point start = gameSettings.start();
        Point end = gameSettings.end();
        maze.setPoint(start, PASSAGE);
        List<Point> toVisit = new ArrayList<>();
        maze.addNeighbors(toVisit, start);

        while (!toVisit.isEmpty()) {
            Point current = pullRandomObject(toVisit);
            if (!maze.checkCellNeighbor(current)) {
                continue;
            }

            maze.setPoint(current, PASSAGE);
            maze.addNeighbors(toVisit, current);
        }
        maze.makePointReachable(end);

        return maze;
    }
}