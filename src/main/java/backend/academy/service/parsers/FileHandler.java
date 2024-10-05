package backend.academy.service.parsers;

public interface FileHandler {
    void createSettings();

    void changeName();

    void save(String path);
}
