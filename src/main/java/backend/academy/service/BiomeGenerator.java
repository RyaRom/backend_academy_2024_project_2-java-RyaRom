package backend.academy.service;

import backend.academy.data.maze.CellType;

public interface BiomeGenerator {
    CellType[][] generate();
}
