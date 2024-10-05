package backend.academy.service.factories;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.exception.IncorrectSettingsException;
import backend.academy.service.solvers.Solver;
import backend.academy.service.solvers.AStarSolver;
import backend.academy.service.solvers.BellmanSolver;
import backend.academy.service.solvers.BfsSolver;
import backend.academy.service.solvers.DfsSolver;
import backend.academy.service.solvers.SpfaSolver;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SolverFactory {
    private final GameSettings gameSettings;

    /**
     * Creates a solver based on the algorithm from game settings
     *
     * @return the solver
     * @throws IncorrectSettingsException if the game settings are invalid
     */
    public Solver solver() {
        if (gameSettings.isInvalid()) {
            throw new IncorrectSettingsException();
        }

        return switch (gameSettings.pathfindingAlgorithm()) {
            case BELLMAN -> new BellmanSolver(gameSettings);
            case A_STAR -> new AStarSolver(gameSettings);
            case DFS -> new DfsSolver(gameSettings);
            case BFS -> new BfsSolver(gameSettings);
            case SPFA -> new SpfaSolver(gameSettings);
        };
    }
}
