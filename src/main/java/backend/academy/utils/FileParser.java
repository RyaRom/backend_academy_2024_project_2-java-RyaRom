package backend.academy.utils;

import backend.academy.data.GameSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@Log4j2
@UtilityClass
public class FileParser {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static GameSettings parseJsonToSettings(File json) {
        try {
            return MAPPER.readValue(json, GameSettings.class);
        } catch (Exception e) {
            log.error("Error parsing JSON file", e);
            throw new RuntimeException(e);
        }
    }

    public static String[] getJsonInDir(String srcPath) {
        File src = new File(srcPath);
        if (!src.isDirectory()) {
            throw new IllegalArgumentException("The path is not a directory: " + src);
        }
        return src.list((f, name) -> name.toLowerCase().endsWith(".json"));
    }

    public static void saveSettingsToFile(GameSettings gameSettings, File json) {
        try {
            MAPPER.writeValue(json, gameSettings);
        } catch (Exception e) {
            log.error("Error saving word list to file", e);
            throw new RuntimeException(e);
        }
    }
}
