package backend.academy.service.parsers;

import java.io.File;

public interface FileParser<T> {

    T readFromFile(File file, Class<T> tClass);

    void writeToFile(T data, File file);

    String[] getJsonInDir(File file);
}