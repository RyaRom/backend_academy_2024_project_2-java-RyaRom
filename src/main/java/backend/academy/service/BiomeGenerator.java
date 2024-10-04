package backend.academy.service;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.CellType;

public interface BiomeGenerator {
    /**
     * Generates a biome grid for maze based on {@link GameSettings}.
     *
     * @return generated biomes
     */
    CellType[][] generate();
}
