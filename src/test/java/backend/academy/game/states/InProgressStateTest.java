package backend.academy.game.states;

import backend.academy.game.GameContext;
import backend.academy.service.generators.Generator;
import backend.academy.service.parsers.CliParser;
import backend.academy.service.renderers.CliRenderer;
import backend.academy.service.renderers.MazeRenderer;
import backend.academy.service.solvers.Solver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
        verify(gameContext, times(1)).init();
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
