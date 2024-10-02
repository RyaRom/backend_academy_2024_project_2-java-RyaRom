package backend.academy.data;

import backend.academy.data.enums.MazeGenerationAlgorithm;
import backend.academy.data.enums.PathfindingAlgorithm;
import backend.academy.data.maze.CellType;
import backend.academy.data.maze.Point;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import static backend.academy.data.GameSettings.DEFAULT_SETTINGS;
import static lombok.Builder.Default;

/**
 * This class represents the mutable version of {@link GameSettings}
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
public final class GameSettingsMutable {
    @Default
    private List<CellType> additionalTypes = DEFAULT_SETTINGS.additionalTypes();

    @Default
    private Character pathRender = DEFAULT_SETTINGS.pathRender();

    @Default
    private Character wallRender = DEFAULT_SETTINGS.wallRender();

    @Default
    private Character passageRender = DEFAULT_SETTINGS.passageRender();

    @Default
    private Integer mazeHeight = DEFAULT_SETTINGS.mazeHeight();

    @Default
    private Integer mazeWidth = DEFAULT_SETTINGS.mazeWidth();

    @Default
    private MazeGenerationAlgorithm generationAlgorithm = DEFAULT_SETTINGS.generationAlgorithm();

    @Default
    private PathfindingAlgorithm pathfindingAlgorithm = DEFAULT_SETTINGS.pathfindingAlgorithm();

    @Default
    private Integer pathRenderSpeedMs = DEFAULT_SETTINGS.pathRenderSpeedMs();

    @Default
    private Point start = DEFAULT_SETTINGS.start();

    @Default
    private Point end = DEFAULT_SETTINGS.end();

    public GameSettings immutable() {
        return GameSettings.builder()
            .additionalTypes(additionalTypes)
            .pathRender(pathRender)
            .wallRender(wallRender)
            .passageRender(passageRender)
            .mazeHeight(mazeHeight)
            .mazeWidth(mazeWidth)
            .generationAlgorithm(generationAlgorithm)
            .pathfindingAlgorithm(pathfindingAlgorithm)
            .pathRenderSpeedMs(pathRenderSpeedMs)
            .start(start)
            .end(end)
            .build();
    }
}
