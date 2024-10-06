package backend.academy.game.states;

import backend.academy.game.GameContext;

/**
 * Represents a state of the game.
 */
public interface GameState {
    /**
     * Executes the game cycle for the given game context.
     *
     * @param gameContext the game context to execute the game cycle for
     */
    void gameCycle(GameContext gameContext);

    /**
     * Switches games state to next
     *
     * @param gameContext the game context to execute the game cycle for
     */
    void nextState(GameContext gameContext);
}
