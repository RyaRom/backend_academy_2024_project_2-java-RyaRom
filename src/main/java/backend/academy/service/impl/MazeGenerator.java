package backend.academy.service.impl;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.service.Generator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MazeGenerator implements Generator {
    private final GameSettings gameSettings;

    @Override
    public Maze generate() {
        return null;
    }
}
