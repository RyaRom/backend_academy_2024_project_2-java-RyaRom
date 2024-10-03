package backend.academy.service.factories;

import backend.academy.data.GameSettings;
import backend.academy.exception.IncorrectSettingsException;
import backend.academy.service.Generator;
import backend.academy.service.generators.KruskalGenerator;
import backend.academy.service.generators.PrimGenerator;
import backend.academy.service.generators.PrimitiveBiomeGenerator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GeneratorFactory {
    private final GameSettings gameSettings;

    /**
     * Creates a generator based on the algorithm from game settings
     *
     * @return the generator
     * @throws IncorrectSettingsException if the settings are incorrect
     */
    public Generator generator() {
        if (gameSettings.isInvalid()) {
            throw new IncorrectSettingsException();
        }

        return switch (gameSettings.generationAlgorithm()) {
            case PRIM -> new PrimGenerator(gameSettings, new PrimitiveBiomeGenerator(gameSettings));
            case KRUSKAL -> new KruskalGenerator(gameSettings);
        };
    }
}
