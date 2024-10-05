package backend.academy.service.factories;

import backend.academy.data.gameSettings.GameSettings;
import backend.academy.service.parsers.CliParser;
import backend.academy.service.parsers.FileHandler;
import backend.academy.service.parsers.FileParser;
import backend.academy.service.parsers.SettingsFileHandler;
import backend.academy.service.renderers.CliRenderer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileHandlerFactory {
    // There is only one impl now but factory makes testing easier
    public FileHandler fileHandler(CliParser parser, CliRenderer renderer, FileParser<GameSettings> fileParser) {
        return new SettingsFileHandler(parser, renderer, fileParser);
    }
}
