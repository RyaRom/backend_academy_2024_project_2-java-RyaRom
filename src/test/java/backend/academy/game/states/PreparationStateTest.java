package backend.academy.game.states;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.maze.Point;
import backend.academy.game.GameContext;
import backend.academy.service.factories.FileHandlerFactory;
import backend.academy.service.parsers.CliParser;
import backend.academy.service.parsers.FileHandler;
import backend.academy.service.parsers.FileParser;
import backend.academy.service.renderers.CliRenderer;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static backend.academy.data.gameSettings.GameSettings.DEFAULT_SETTINGS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PreparationStateTest {

    @Mock
    private CliRenderer renderer;

    @Mock
    private CliParser parser;

    @Mock
    private FileParser<GameSettings> fileParser;

    @Mock
    private GameContext gameContext;

    @Mock
    private File settingsLocation;

    @Mock
    private FileHandlerFactory fileHandlerFactory;

    @BeforeEach
    void setUp() {
        when(gameContext.defaultSettings()).thenReturn(DEFAULT_SETTINGS);
    }

    @Test
    void loadIncorrectSettings() {
        GameSettings incorrect = GameSettings.builder()
            .start(Point.of(100, 100))
            .end(Point.of(100, 100))
            .mazeHeight(1)
            .mazeWidth(1)
            .build();
        when(fileParser.getJsonInDir(any())).thenReturn(new String[1]);
        when(fileParser.readFromFile(any(), any())).thenReturn(incorrect);
        when(parser.readCommand(anyInt(), anyInt())).thenReturn(
            2, 0, 3, 0, 2, 1, 2, 2, 0
        );

        PreparationState preparationState =
            new PreparationState(renderer, parser, fileParser, settingsLocation, fileHandlerFactory);
        preparationState.gameCycle(gameContext);

        verify(gameContext, times(1)).exit();
        verify(gameContext, never()).start(any(), any(), any(), any(), any());
    }

    @Test
    void createAndLoadCorrectSettings() {
        FileHandler fileHandler = mock(FileHandler.class);
        when(fileHandlerFactory.fileHandler(any(), any(), any())).thenReturn(fileHandler);
        when(fileParser.getJsonInDir(any())).thenReturn(new String[1]);
        when(parser.readCommand(anyInt(), anyInt())).thenReturn(
            3, 1, 2, 3, 2, 1, 2, 0, 1
        );

        PreparationState preparationState =
            new PreparationState(renderer, parser, fileParser, settingsLocation, fileHandlerFactory);
        preparationState.gameCycle(gameContext);

        verify(fileHandler, times(1)).createSettings();
        verify(fileHandler, times(1)).changeName();
        verify(fileHandler, times(1)).save(any());
        verify(gameContext, times(1)).start(any(), any(), any(), any(), any());
    }
}
