package at.helpch.papibot.core.objects.paginations;

// ------------------------------
// Copyright (c) PiggyPiglet 2019
// https://www.piggypiglet.me
// ------------------------------
public final class PageNotFoundException extends Exception {
    public PageNotFoundException(String message) {
        super(message);
    }
}
