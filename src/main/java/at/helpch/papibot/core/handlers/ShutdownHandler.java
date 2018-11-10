package at.helpch.papibot.core.handlers;

import at.helpch.papibot.PapiBot;
import at.helpch.papibot.core.objects.tasks.Task;
import co.aikar.idb.DB;
import com.google.inject.Inject;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ShutdownHandler extends Thread {
    @Inject private PapiBot papiBot;

    @Override
    public void run() {
        papiBot.getJda().shutdownNow();
        Task.shutdown();
        DB.close();
    }
}
