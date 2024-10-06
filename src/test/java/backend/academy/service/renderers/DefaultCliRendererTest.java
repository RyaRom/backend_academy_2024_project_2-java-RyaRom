package backend.academy.service.renderers;

import java.io.PrintStream;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DefaultCliRendererTest {

    private DefaultCliRenderer cliRenderer;

    @Mock
    private PrintStream outputWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliRenderer = new DefaultCliRenderer(outputWriter);
    }

    @Test
    void testRenderIntro() {
        cliRenderer.renderIntro();
        verify(outputWriter).println(any(String.class));
    }

    @Test
    void testRenderMenuWithEmptyMenu() {
        assertThrows(IllegalArgumentException.class, () -> cliRenderer.renderMenu(List.of()));
    }

    @Test
    void testClearScreen() {
        cliRenderer.clearScreen();

        verify(outputWriter, times(20)).println();
        verify(outputWriter).print("\033[H\033[2J");
        verify(outputWriter).flush();
    }

    @Test
    void testPrintln() {
        String message = "Hello, World!";
        cliRenderer.println(message);
        verify(outputWriter).println(message);
    }

    @Test
    void testNewLine() {
        cliRenderer.newLine();
        verify(outputWriter).println();
    }
}
