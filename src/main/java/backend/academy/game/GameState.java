package backend.academy.game;

public interface GameState {
    void gameCycle(GameContext gameContext);

    void nextState(GameContext gameContext);
}
