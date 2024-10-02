package backend.academy.service.renderers;

import backend.academy.service.CliRenderer;
import java.io.PrintStream;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultCliRenderer implements CliRenderer {
    private final PrintStream outputWriter;

    private final String intro = String.join(System.lineSeparator(),
        "██╗      █████╗ ██████╗ ██╗   ██╗██████╗ ██╗███╗   ██╗████████╗██╗  ██╗███████╗",
        "██║     ██╔══██╗██╔══██╗╚██╗ ██╔╝██╔══██╗██║████╗  ██║╚══██╔══╝██║  ██║██╔════╝",
        "██║     ███████║██████╔╝ ╚████╔╝ ██████╔╝██║██╔██╗ ██║   ██║   ███████║███████╗",
        "██║     ██╔══██║██╔══██╗  ╚██╔╝  ██╔══██╗██║██║╚██╗██║   ██║   ██╔══██║╚════██║",
        "███████╗██║  ██║██████╔╝   ██║   ██║  ██║██║██║ ╚████║   ██║   ██║  ██║███████║",
        "╚══════╝╚═╝  ╚═╝╚═════╝    ╚═╝   ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝   ╚═╝   ╚═╝  ╚═╝╚══════╝",
        ""
    );

    @Override
    public void renderMenu(List<String> options, String menuName) {
        if (menuName == null || options == null || options.isEmpty()) {
            throw new IllegalArgumentException("No string from the empty array");
        }

        StringBuilder stringBuilder = new StringBuilder()
            .append("Select ")
            .append(menuName)
            .append(":")
            .append(System.lineSeparator())
            .append("0. Go back")
            .append(System.lineSeparator());

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i) == null) {
                continue;
            }
            stringBuilder.append(i + 1)
                .append(". ")
                .append(options.get(i))
                .append(System.lineSeparator());
        }
        stringBuilder.append("Enter your choice: ");
        outputWriter.println(stringBuilder);
    }

    @Override
    public void renderMenu(List<String> options) {
        renderMenu(options, "option");
    }

    @Override
    public void renderIntro() {
        outputWriter.println(intro);
    }

    @Override
    public void clearScreen() {
        outputWriter.print("\033[H\033[2J");
        outputWriter.flush();
    }
}
