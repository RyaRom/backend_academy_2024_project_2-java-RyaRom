package backend.academy.data;

import backend.academy.data.maze.CellType;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import static lombok.Builder.Default;

@Getter
@Setter
@Builder
public final class GameSettings {
    public static final GameSettings DEFAULT_SETTINGS = builder().build();

    @Default
    private final Map<Long, CellType> cellTypes = Map.of(
        0L, new CellType(0, true, 1, ' '),
        1L, new CellType(1, false, Integer.MAX_VALUE, '█'),
        2L, new CellType(2, true, 5, '░'),
        3L, new CellType(3, true, -10, '₿')
    );
    //TODO: add parameters for maze generation and pathfinding
}
