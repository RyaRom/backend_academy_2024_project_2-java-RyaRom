package backend.academy.game.states;

import backend.academy.game.GameContext;

public interface GameState {
    void gameCycle(GameContext gameContext);

    void nextState(GameContext gameContext);
}
