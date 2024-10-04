package backend.academy.service;

import java.util.List;

public interface CliRenderer {
    /**
     * Renders the menu for CLI
     *
     * @param options  the options to render
     * @param menuName the name of the menu "Select {@code menuName}:"
     * @throws IllegalArgumentException if the options are empty or null
     */

    void renderMenu(List<String> options, String menuName);

    /**
     * Renders the menu for CLI with menu name "Select option:"
     *
     * @param options the options to render
     * @throws IllegalArgumentException if the options are empty or null
     */
    void renderMenu(List<String> options);

    /**
     * Renders the game intro
     */
    void renderIntro();

    /**
     * Clears the terminal
     */
    void clearScreen();

    void println(String message);

    void newLine();
}
