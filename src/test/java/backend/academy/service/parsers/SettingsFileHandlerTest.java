package backend.academy.service.parsers;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.gameSettings.MutableGameSettings;
import backend.academy.data.maze.CellType;
import backend.academy.service.renderers.CliRenderer;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SettingsFileHandlerTest {
    @Mock
    private CliParser parser;

    @Mock
    private CliRenderer renderer;

    @Mock
    private FileParser<GameSettings> fileParser;

    private SettingsFileHandler settingsFileHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        settingsFileHandler = new SettingsFileHandler(parser, renderer, fileParser);
    }

    @Test
    void testCreateSettingsWithValidInput() {
        when(parser.read(anyString())).thenReturn(
            "20",
            "20",
            "0",
            "1.0",
            "·",
            "█",
            " ",
            "S",
            "F",
            "PRIM",
            "SPFA",
            "0 0",
            "1 1",
            "y",
            "true",
            "0",
            "*",
            "n"
        );

        settingsFileHandler.createSettings();

        verify(renderer, times(1)).println(contains("maze height"));
        verify(renderer, times(1)).println(contains("maze width"));
        verify(renderer, times(1)).println(contains("path render speed in ms"));
        verify(renderer, times(1)).println(contains("biomes frequency"));
        verify(renderer, times(1)).println(contains("pathRender"));
        verify(renderer, times(1)).println(contains("wallRender"));
        verify(renderer, times(1)).println(contains("passageRender"));
        verify(renderer, times(1)).println(contains("generation algorithm"));
        verify(renderer, times(1)).println(contains("pathfinding algorithm"));
        verify(renderer, times(1)).println(contains("start point"));
        verify(renderer, times(1)).println(contains("end point"));
        verify(renderer, times(1)).println(contains("Would you like to add custom cell types?"));

        verify(parser, times(18)).read(anyString());
        var settings = MutableGameSettings.builder().build();
        settings.additionalTypes(List.of(new CellType(true, 0, '*')));
        assertEquals(settings, settingsFileHandler.gameSettings());
    }

    @Test
    void testCreateSettingsWithInvalidInput() {
        when(parser.read(anyString())).thenReturn(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "smth",
            "smth",
            "",
            "",
            "n"
        );

        settingsFileHandler.createSettings();

        verify(renderer, atLeastOnce()).println(contains("Invalid algorithm. Default will be used."));
        assertEquals(MutableGameSettings.builder().build(), settingsFileHandler.gameSettings());
    }

    @Test
    void testSave() {
        String path = "src/test/resources";
        String expectedFileName = path + "/" + settingsFileHandler.name() + "_settings.json";
        File expectedFile = new File(expectedFileName);

        settingsFileHandler.save(path);

        verify(fileParser).writeToFile(any(GameSettings.class), eq(expectedFile));
    }

    @Test
    void testChangeName() {
        when(parser.read("[a-zA-Z]*")).thenReturn("newName");

        settingsFileHandler.changeName();

        assertEquals("newName", settingsFileHandler.name());
        verify(renderer).println(contains("Enter new file name:"));
    }
}
