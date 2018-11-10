package at.helpch.papibot;

import at.helpch.papibot.core.framework.BinderModule;
import com.google.inject.Injector;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PapiBotBootstrap {
    private static final Class CLASS = PapiBotBootstrap.class;

    public static void main(String[] args) throws Exception {
        Injector injector = new BinderModule(CLASS).createInjector();
        injector.injectMembers(CLASS.newInstance());

        injector.getInstance(PapiBot.class).start(injector);
    }
}
