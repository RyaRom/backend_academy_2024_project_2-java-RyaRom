package backend.academy.game.states;

import backend.academy.data.maze.Maze;
import backend.academy.data.maze.Point;
import backend.academy.game.GameContext;
import backend.academy.service.generators.Generator;
import backend.academy.service.parsers.CliParser;
import backend.academy.service.renderers.CliRenderer;
import backend.academy.service.renderers.MazeRenderer;
import backend.academy.service.solvers.Solver;
import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * This class is representing in progress state of the game
 * <p>
 * This state is used to solve maze and render maze with solution
 * </p>
 */
@RequiredArgsConstructor
public class InProgressState implements GameState {
    private final CliRenderer cliRenderer;

    private final CliParser cliParser;

    private final MazeRenderer mazeRenderer;

    private final Generator generator;

    private final Solver solver;

    @Override
    public void gameCycle(GameContext gameContext) {
        Maze maze = generator.generate();
        renderMaze(maze);
        List<Point> path = solver.solve(maze);
        renderPath(maze, path);

        cliRenderer.println("Would you like to continue?");
        cliRenderer.renderMenu(List.of("Exit"));
        int menuChoice = cliParser.readCommand(0, 1);
        if (menuChoice == 1) {
            gameContext.exit();
        } else {
            nextState(gameContext);
        }
    }

    private void renderMaze(Maze maze) {
        cliRenderer.println("Maze is generated:");
        cliRenderer.newLine();
        mazeRenderer.render(maze);
        cliRenderer.newLine();
    }

    private void renderPath(Maze maze, List<Point> path) {
        cliRenderer.println("Generating path:");
        cliRenderer.newLine();
        mazeRenderer.render(maze, path);
    }

    @Override
    public void nextState(GameContext gameContext) {
        gameContext.init(gameContext.defaultSettings());
    }
}
