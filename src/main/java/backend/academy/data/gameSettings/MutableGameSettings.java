package backend.academy.data.gameSettings;

import backend.academy.data.enums.MazeGenerationAlgorithm;
import backend.academy.data.enums.PathfindingAlgorithm;
import backend.academy.data.maze.CellType;
import backend.academy.data.maze.Point;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import static backend.academy.data.gameSettings.GameSettings.DEFAULT_SETTINGS;
import static lombok.Builder.Default;

/**
 * This class represents the mutable version of {@link GameSettings}
 */
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public final class MutableGameSettings {
    @Default
    private List<CellType> additionalTypes = DEFAULT_SETTINGS.additionalTypes();

    @Default
    private Character pathRender = DEFAULT_SETTINGS.pathRender();

    @Default
    private Character wallRender = DEFAULT_SETTINGS.wallRender();

    @Default
    private Character passageRender = DEFAULT_SETTINGS.passageRender();

    @Default
    private Character startRender = DEFAULT_SETTINGS.startRender();

    @Default
    private Character endRender = DEFAULT_SETTINGS.endRender();

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

    @Default
    private Double biomesFreq = DEFAULT_SETTINGS.biomesFreq();

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
            .biomesFreq(biomesFreq)
            .startRender(startRender)
            .endRender(endRender)
            .build();
    }
}
