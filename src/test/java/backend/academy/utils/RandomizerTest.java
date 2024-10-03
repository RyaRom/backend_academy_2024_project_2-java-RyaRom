package backend.academy.utils;

import backend.academy.data.maze.Cell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import static backend.academy.utils.Randomizer.pullRandomObject;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomizerTest {
    @Test
    void pickRandomObjectTest() {
        List<Cell> cells = Instancio.createList(Cell.class);
        List<Cell> cellsCopy = new ArrayList<>(cells);
        Cell cell = pullRandomObject(cells);

        assertThatThrownBy(() -> pullRandomObject(new ArrayList<>()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("No random element in the empty array");
        assertEquals("", pullRandomObject(new ArrayList<>(Collections.singleton(""))));
        assertTrue(cellsCopy.contains(cell));
        assertFalse(cells.contains(cell));
        assertEquals(cells.size(), cellsCopy.size() - 1);
    }

}
