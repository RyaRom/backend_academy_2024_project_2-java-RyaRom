package backend.academy.service.parsers;

import backend.academy.data.gameSettings.GameSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static backend.academy.data.gameSettings.GameSettings.DEFAULT_SETTINGS;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsonParserTest {
    private final ObjectMapper mapper = mock(ObjectMapper.class);

    private final JsonParser<GameSettings> jsonParser = new JsonParser<>(mapper);

    @Test
    void testReadFromFile() throws Exception {
        File file = new File("test.json");
        Class<GameSettings> classType = GameSettings.class;
        GameSettings expectedObject = DEFAULT_SETTINGS;
        when(mapper.readValue(file, classType)).thenReturn(expectedObject);

        GameSettings result = jsonParser.readFromFile(file, classType);

        assertEquals(expectedObject, result);
        verify(mapper).readValue(file, classType);
    }

    @Test
    void testReadFromFileThrowsRuntimeException() throws IOException {
        File file = mock(File.class);
        Class<GameSettings> classType = GameSettings.class;
        when(mapper.readValue(file, classType)).thenThrow(new IOException());

        assertThrows(RuntimeException.class, () -> jsonParser.readFromFile(file, classType));
        verify(mapper).readValue(file, classType);
    }

    @Test
    void testWriteToFile() throws Exception {
        GameSettings data = DEFAULT_SETTINGS;
        File file = new File("output.json");
        jsonParser.writeToFile(data, file);

        verify(mapper).writeValue(file, data);
    }

    @Test
    void testWriteToFileHandlesException() throws IOException {
        GameSettings data = DEFAULT_SETTINGS;
        File file = mock(File.class);
        doThrow(new IOException()).when(mapper).writeValue(file, data);

        assertDoesNotThrow(() -> jsonParser.writeToFile(data, file));

        verify(mapper).writeValue(file, data);
    }

    @Test
    void testGetJsonInDirReturnsJsonFiles() {
        File dir = mock(File.class);
        var files = new String[] {"file1.json", "file2.txt", "file3.json"};
        when(dir.isDirectory()).thenReturn(true);
        when(dir.list(any())).thenReturn(files);
        String[] result = jsonParser.getJsonInDir(dir);

        assertArrayEquals(files, result);
    }

    @Test
    void testGetJsonInDirReturnsEmptyArrayForNonDirectory() {
        File file = mock(File.class);
        when(file.isDirectory()).thenReturn(false);
        String[] result = jsonParser.getJsonInDir(file);

        assertArrayEquals(new String[0], result);
    }
}
