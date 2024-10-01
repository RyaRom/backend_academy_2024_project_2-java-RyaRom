package backend.academy.service;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;

public interface Generator {
    Maze generate();
}
