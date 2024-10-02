package backend.academy.service;

import backend.academy.data.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.exception.IncorrectSettingsException;

public interface Generator {
    /**
     * Generates a maze. Takes generation parameters from {@link GameSettings}
     *
     * @return generated maze
     * @throws IncorrectSettingsException if game settings are incorrect
     * @see GameSettings
     */
    Maze generate();
}
