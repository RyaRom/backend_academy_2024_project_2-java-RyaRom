package backend.academy.game;

import backend.academy.data.GameSettings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static backend.academy.data.GameSettings.DEFAULT_SETTINGS;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameContext {
    private GameSettings gameSettings = DEFAULT_SETTINGS;
}
