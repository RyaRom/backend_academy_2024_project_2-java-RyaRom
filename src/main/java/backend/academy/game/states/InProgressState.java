package backend.academy.game.states;

import backend.academy.game.GameContext;
import backend.academy.game.GameState;
import backend.academy.service.CliParser;
import backend.academy.service.CliRenderer;
import backend.academy.service.Generator;
import backend.academy.service.MazeRenderer;
import backend.academy.service.Solver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InProgressState implements GameState {
    private final CliRenderer cliRenderer;

    private final CliParser cliParser;

    private final MazeRenderer mazeRenderer;

    private final Generator generator;

    private final Solver solver;

    @Override
    public void gameCycle(GameContext gameContext) {

    }

    @Override
    public void nextState(GameContext gameContext) {

    }
}
