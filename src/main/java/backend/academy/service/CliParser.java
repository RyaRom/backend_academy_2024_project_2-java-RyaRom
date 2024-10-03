package backend.academy.service;

public interface CliParser {

    /**
     * Reads user input and returns command until command is correct
     *
     * @param lowerBound lower bound of possible command
     * @param upperBound upper bound of possible command
     * @return command or -1 if there is no correct command in buffer
     */
    Integer readCommand(
        int lowerBound,
        int upperBound
    );

    /**
     * Reads user input and returns command until command is correct
     *
     * @param regex regex to match
     * @return command or "" if there is no correct command in buffer
     */
    String read(String regex);
}
