package backend.academy.game;

import backend.academy.data.GameSettings;
import backend.academy.game.states.PreparationState;
import backend.academy.service.renderers.DefaultCliRenderer;
import java.io.BufferedReader;
import java.io.PrintStream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import static backend.academy.data.GameSettings.DEFAULT_SETTINGS;

@Getter
@Setter
@RequiredArgsConstructor
public class GameContext {
    private final BufferedReader inputReader;

    private final PrintStream outputWriter;

    private GameSettings gameSettings = DEFAULT_SETTINGS;

    public void init() {
        PreparationState state = new PreparationState(
            new DefaultCliRenderer(outputWriter)
        );
        state.gameCycle(this);
    }
}
