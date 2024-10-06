package backend.academy.service.parsers;

import java.io.File;

/**
 * Interface for parsing files
 */
public interface FileParser<T> {

    /**
     * Read from file
     *
     * @param file   file to read from
     * @param tClass class of the object to read
     * @return object of type T
     */
    T readFromFile(File file, Class<T> tClass);

    /**
     * Write to file
     *
     * @param data data to write
     * @param file file to write to
     */
    void writeToFile(T data, File file);

    /**
     * Get all json files in a directory.
     * Checks by looking if file ends with .json
     *
     * @param file directory to search in
     * @return array of json files
     */
    String[] getJsonInDir(File file);
}
