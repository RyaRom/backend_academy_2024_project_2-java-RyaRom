package backend.academy;

import backend.academy.game.GameContext;
import lombok.experimental.UtilityClass;
import static backend.academy.data.gameSettings.GameSettings.DEFAULT_SETTINGS;
import static backend.academy.game.GameContext.CONSOLE_READER;
import static backend.academy.game.GameContext.CONSOLE_WRITER;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        GameContext gameContext = new GameContext(CONSOLE_READER, CONSOLE_WRITER);
        gameContext.init(DEFAULT_SETTINGS);
    }
}
