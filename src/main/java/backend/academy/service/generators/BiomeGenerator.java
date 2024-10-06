package backend.academy.service.generators;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.maze.CellType;

/**
 * Generates maze biomes.
 *
 * <p>
 * Biome grid is a 2D array of {@link CellType} that represents the maze.
 * When maze is generated, instead of passages it picks type from biomes grid
 * </p>
 */
public interface BiomeGenerator {
    /**
     * Generates a biome grid for maze based on {@link GameSettings}.
     *
     * @return generated biomes
     */
    CellType[][] generate();
}
