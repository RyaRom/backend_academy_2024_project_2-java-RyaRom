package backend.academy.game.states;

import backend.academy.game.GameContext;
import backend.academy.service.generators.Generator;
import backend.academy.service.parsers.CliParser;
import backend.academy.service.renderers.CliRenderer;
import backend.academy.service.renderers.MazeRenderer;
import backend.academy.service.solvers.Solver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InProgressStateTest {

    @Mock
    private CliRenderer cliRenderer;

    @Mock
    private CliParser cliParser;

    @Mock
    private MazeRenderer mazeRenderer;

    @Mock
    private Generator generator;

    @Mock
    private Solver solver;

    @Mock
    private GameContext gameContext;

    @Test
    void flow() {
        when(cliParser.readCommand(anyInt(), anyInt())).thenReturn(0);
        InProgressState inProgressState
            = new InProgressState(cliRenderer, cliParser, mazeRenderer, generator, solver);
        inProgressState.gameCycle(gameContext);

        verify(generator, times(1)).generate();
        verify(solver, times(1)).solve(any());
        verify(mazeRenderer, times(1)).render(any());
        verify(mazeRenderer, times(1)).render(any(), any());
        verify(gameContext, times(1)).init(any());
    }

    @Test
    void exit() {
        when(cliParser.readCommand(anyInt(), anyInt())).thenReturn(1);
        InProgressState inProgressState
            = new InProgressState(cliRenderer, cliParser, mazeRenderer, generator, solver);
        inProgressState.gameCycle(gameContext);

        verify(generator, times(1)).generate();
        verify(solver, times(1)).solve(any());
        verify(mazeRenderer, times(1)).render(any());
        verify(mazeRenderer, times(1)).render(any(), any());
        verify(gameContext, times(1)).exit();
    }
}
