package backend.academy.service.generators;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.CellType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static backend.academy.data.maze.CellType.PASSAGE;
import static backend.academy.data.maze.CellType.WALL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BiomeGeneratorsTest {
    private final CellType cellA = new CellType(true, 1, 'A');

    private final CellType cellB = new CellType(true, 2, 'B');

    private final CellType cellC = new CellType(true, 1, 'C');

    private final CellType cellD = new CellType(false, 1, 'D');

    private final GameSettings gameSettings = GameSettings.builder()
        .additionalTypes(List.of(cellA, cellB, cellC, cellD))
        .build();

    private PrimitiveBiomeGenerator generator;

    @Test
    void generate() {
        generator = new PrimitiveBiomeGenerator(gameSettings);
        var biome = generator.generate();

        for (var arr : biome) {
            for (var cell : arr) {
                System.out.print(cell.render());
            }
            System.out.println();
        }

        var biomeSet = Arrays.stream(biome)
            .flatMap(Arrays::stream)
            .collect(Collectors.toSet());
        assertTrue(biomeSet.contains(PASSAGE));
        assertTrue(biomeSet.contains(cellA) || biomeSet.contains(cellB) || biomeSet.contains(cellC));
        assertFalse(biomeSet.contains(cellD));
    }
}
