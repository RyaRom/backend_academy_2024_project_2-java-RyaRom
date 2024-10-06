package backend.academy.service.parsers;

/**
 * Class for maintaining creation of custom settings by user
 */
public interface FileHandler {
    /**
     * Method for creating custom settings from user input
     */
    void createSettings();

    /**
     * Method for changing name of custom settings file.
     * By default, name is randomized
     */
    void changeName();

    /**
     * Method for saving custom settings to file
     *
     * @param path path to file
     */
    void save(String path);
}
