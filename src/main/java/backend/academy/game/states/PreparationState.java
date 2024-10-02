package backend.academy.game.states;

import backend.academy.game.GameContext;
import backend.academy.game.GameState;
import backend.academy.service.CliRenderer;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PreparationState implements GameState {
    private final CliRenderer renderer;

    private final List<String> startMenu =
        List.of(
            "Generate labyrinth",
            "Load settings configuration",
            "Create new settings configuration"
        );

    @Override
    public void gameCycle(GameContext gameContext) {
        renderer.renderIntro();
        renderer.renderMenu(startMenu);
    }

    @Override
    public void nextState(GameContext gameContext) {

    }
}
