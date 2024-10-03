package backend.academy.utils;

import backend.academy.data.maze.Cell;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import static backend.academy.utils.Randomizer.binaryCeiling;
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

    @Test
    void binary() {
        List<Double> list = new ArrayList<>() {{
            add(1.0);
            add(2.0);
            add(3.0);
            add(3.0);
            add(3.0);
            add(4.0);
            add(5.0);
        }};

        assertEquals(0, binaryCeiling(list, -100.0));
        assertEquals(0, binaryCeiling(list, 0.5));
        assertEquals(0, binaryCeiling(list, 1.0));
        assertEquals(1, binaryCeiling(list, 1.5));
        assertEquals(2, binaryCeiling(list, 2.5));
        assertEquals(5, binaryCeiling(list, 3.9));
        assertEquals(6, binaryCeiling(list, 4.5));
        assertEquals(6, binaryCeiling(list, 5.0));
        assertEquals(7, binaryCeiling(list, 100.0));
    }

    @Test
    void getRandomWeightedObject() {
        Map<Integer, Double> map = Map.of(1, 0.1, 2, 0.2, 3, 0.3, 4, 2.0);
        assertTrue(map.containsKey(Randomizer.getRandomWeightedObject(map)));
    }
}
