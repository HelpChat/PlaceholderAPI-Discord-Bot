package at.helpch.papibot.core.utils.string;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class StringUtils {
    public static boolean startsWith(String str, String[] contain) {
        return lowercaseParallelStream(Arrays.asList(contain)).anyMatch(str.toLowerCase()::startsWith);
    }

    public static boolean equalsIgnoreCase(String str, String[] strings) {
        return lowercaseParallelStream(Arrays.asList(strings)).anyMatch(str.toLowerCase()::equalsIgnoreCase);
    }

    private static Stream<String> lowercaseParallelStream(List<String> list) {
        return list.stream().map(String::toLowerCase).parallel();
    }

    public static List<String> replaceAll(List<String> list, String key, String regex) {
        return list.stream().map(i -> i = i.replace(key, regex)).collect(Collectors.toList());
    }
}
