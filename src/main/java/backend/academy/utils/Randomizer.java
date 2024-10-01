package backend.academy.utils;

import java.security.SecureRandom;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Randomizer {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static <T> T pullRandomObject(List<T> objects) {
        if (objects.isEmpty()) {
            throw new IllegalArgumentException("No random element in the empty array");
        }

        int index = RANDOM.nextInt(objects.size());
        return objects.remove(index);
    }
}
