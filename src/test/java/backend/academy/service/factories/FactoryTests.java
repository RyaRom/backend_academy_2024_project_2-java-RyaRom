package backend.academy.service.factories;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.maze.Point;
import backend.academy.exception.IncorrectSettingsException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FactoryTests {

    private GameSettings gameSettings;

    private SolverFactory solverFactory;

    private GeneratorFactory generatorFactory;

    @Test
    void incorrectSettings() {
        gameSettings = GameSettings.builder()
            .start(Point.of(100, 100))
            .biomesFreq(-1.0)
            .mazeHeight(1)
            .mazeWidth(1)
            .build();

        solverFactory = new SolverFactory(gameSettings);
        generatorFactory = new GeneratorFactory(gameSettings);

        assertThrows(IncorrectSettingsException.class, () -> solverFactory.solver());
        assertThrows(IncorrectSettingsException.class, () -> generatorFactory.generator());
    }
}
