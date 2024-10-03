package backend.academy.service.parsers;

import backend.academy.service.FileParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class JsonParser<T> implements FileParser<T> {

    private final ObjectMapper mapper;

    @Override
    public T readFromFile(File file, Class<T> tClass) {
        try {
            return mapper.readValue(file, tClass);
        } catch (Exception e) {
            log.error("Error parsing JSON file", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeToFile(T data, File file) {
        try {
            mapper.writeValue(file, data);
        } catch (Exception e) {
            log.error("Error saving word list to file", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String[] getJsonInDir(File file) {
        if (!file.isDirectory()) {
            return new String[0];
        }
        return file.list((f, name) -> name.toLowerCase().endsWith(".json"));
    }
}
