package backend.academy.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("MultipleStringLiterals")
public class Randomizer {
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Returns the first element greater than the key
     *
     * @param list - sorted list
     * @param key  - key to search
     * @return index of next bigger or equals element of key
     */
    public static <T> int binaryCeiling(List<? extends Comparable<? super T>> list, T key) {
        int ret = Collections.binarySearch(list, key);
        if (ret < 0) {
            return -ret - 1;
        }
        return ret;
    }

    /**
     * Pulls a random object from a list and removes it from the list
     *
     * @param objects - list of objects
     * @param <T>     - type of objects in the list
     * @return random object from the list
     * @throws IllegalArgumentException if the list is empty
     */
    public static <T> T pullRandomObject(List<T> objects) {
        if (objects.isEmpty()) {
            throw new IllegalArgumentException("No random element in the empty array");
        }

        int index = RANDOM.nextInt(objects.size());
        return objects.remove(index);
    }

    /**
     * Pulls a random object from a list
     *
     * @param objects - list of objects
     * @param <T>     - type of objects in the list
     * @return random object from the list
     * @throws IllegalArgumentException if the list is empty
     */
    public static <T> T getRandomObject(List<T> objects) {
        if (objects.isEmpty()) {
            throw new IllegalArgumentException("No random element in the empty array");
        }

        int index = RANDOM.nextInt(objects.size());
        return objects.get(index);
    }

    /**
     * Returns a random object from a map with weights
     *
     * @param objects - map of objects and their weights
     * @param <T>     - type of objects in the map
     * @return random object from the map
     * @throws IllegalArgumentException if the map is empty
     */
    public static <T> T getRandomWeightedObject(Map<T, Double> objects) {
        if (objects.isEmpty()) {
            throw new IllegalArgumentException("No random element in the empty array");
        }

        var keys = objects.keySet().stream().toList();
        List<Double> prefixSum = new ArrayList<>();
        Double totalSum = 0.0;
        for (T key : keys) {
            totalSum += objects.get(key);
            prefixSum.add(totalSum);
        }

        Double random = getRandomDouble(0.0, totalSum);
        int i = binaryCeiling(prefixSum, random);
        return keys.get(i);
    }

    /**
     * Returns a random int from 0 to bound
     *
     * @return random int from 0 to bound
     */
    public static String getRandomInt(int bound) {
        return String.valueOf(RANDOM.nextInt(bound));
    }

    public static Double getRandomDouble(double origin, double bound) {
        return RANDOM.nextDouble(origin, bound);
    }
}
