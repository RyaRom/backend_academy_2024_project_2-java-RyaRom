package backend.academy.service.generators;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.CellType;
import backend.academy.data.maze.Point;
import backend.academy.service.BiomeGenerator;
import backend.academy.utils.Randomizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import static backend.academy.data.maze.CellType.PASSAGE;
import static backend.academy.utils.Randomizer.getRandomDouble;
import static backend.academy.utils.Randomizer.getRandomWeightedObject;

@RequiredArgsConstructor
public class PrimitiveBiomeGenerator implements BiomeGenerator {
    private final GameSettings gameSettings;

    //TODO: add this to game settings or add prob map in additional types in settings
    private final Double biomesFreq = 2.0;

    private Map<CellType, Double> weighedTypes;

    @Override
    public CellType[][] generate() {
        List<CellType> types = new ArrayList<>(gameSettings.additionalTypes());
        types.add(PASSAGE);
        types = types.stream()
            .filter(CellType::isPassage)
            .distinct()
            .toList();
        weighedTypes = types.stream()
            .collect(Collectors.toMap(type -> type, prob -> getRandomDouble(0.0, biomesFreq)));
        weighedTypes.put(PASSAGE, 2.0);

        var biomes = new CellType[gameSettings.mazeHeight()][gameSettings.mazeWidth()];

        for (int i = 0; i < gameSettings.mazeHeight(); i++) {
            for (int j = 0; j < gameSettings.mazeWidth(); j++) {
                if (biomes[i][j] == null) {
                    generateBiome(Point.of(i, j), biomes);
                }
            }
        }
        return biomes;
    }

    private List<Point> getEmptyNeighbours(int x, int y, CellType[][] biomes) {
        List<Point> neighbours = new ArrayList<>();
        if (x > 0 && biomes[x - 1][y] == null) {
            neighbours.add(new Point(x - 1, y));
        }
        if (x < biomes.length - 1 && biomes[x + 1][y] == null) {
            neighbours.add(new Point(x + 1, y));
        }
        if (y > 0 && biomes[x][y - 1] == null) {
            neighbours.add(new Point(x, y - 1));
        }
        if (y < biomes[0].length - 1 && biomes[x][y + 1] == null) {
            neighbours.add(new Point(x, y + 1));
        }
        return neighbours;
    }

    //TODO add some docs here
    @SuppressWarnings("MagicNumber")
    private void generateBiome(Point point, CellType[][] biomes) {
        int height = gameSettings.mazeHeight();
        int width = gameSettings.mazeWidth();
        CellType cellType = getRandomWeightedObject(weighedTypes);
        biomes[point.row()][point.col()] = cellType;
        int bound = Integer.parseInt(Randomizer.getRandomInt((int) (((double) (height * width) / 30) * biomesFreq)));
        List<Point> neighbours = getEmptyNeighbours(point.row(), point.col(), biomes);

        while (!neighbours.isEmpty() && bound > 0) {
            Point neighbour = Randomizer.pullRandomObject(neighbours);
            biomes[neighbour.row()][neighbour.col()] = cellType;
            neighbours.addAll(getEmptyNeighbours(neighbour.row(), neighbour.col(), biomes));
            bound--;
        }
    }
}
