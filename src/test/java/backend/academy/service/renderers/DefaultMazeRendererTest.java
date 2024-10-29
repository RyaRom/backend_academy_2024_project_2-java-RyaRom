package backend.academy.service.renderers;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.data.maze.Maze;
import backend.academy.service.factories.GeneratorFactory;
import org.junit.jupiter.api.Test;

class DefaultMazeRendererTest {

    @Test
    void renderAscii() {
        GameSettings settings = GameSettings.builder()
            .asciiMode(true)
            .mazeHeight(20)
            .mazeHeight(20)
            .biomesFreq(0.0)
            .build();
        DefaultMazeRenderer renderer = new DefaultMazeRenderer(
            settings,
            System.out
        );
        Maze maze = new GeneratorFactory(settings).generator().generate();
        renderer.render(maze);
    }
}
