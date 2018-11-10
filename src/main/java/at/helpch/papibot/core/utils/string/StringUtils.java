package at.helpch.papibot.core.utils.string;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class StringUtils {
    public static boolean startsWith(String str, String[] contain) {
        return lowercaseParallelStream(Arrays.asList(contain)).anyMatch(str.toLowerCase()::startsWith);
    }

    private static Stream<String> lowercaseParallelStream(List<String> list) {
        return list.stream().map(String::toLowerCase).parallel();
    }
}
