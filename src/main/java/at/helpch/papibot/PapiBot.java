package at.helpch.papibot;

import at.helpch.papibot.core.framework.Command;
import at.helpch.papibot.core.handlers.EventHandler;
import at.helpch.papibot.core.handlers.GEvent;
import at.helpch.papibot.core.handlers.chat.CommandHandler;
import at.helpch.papibot.core.objects.enums.Registerables;
import at.helpch.papibot.core.objects.tasks.GRunnable;
import at.helpch.papibot.core.objects.tasks.Task;
import at.helpch.papibot.core.storage.file.GFile;
import at.helpch.papibot.core.storage.mysql.MySQLInitializer;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import lombok.Getter;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.reflections.Reflections;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

import static at.helpch.papibot.core.objects.enums.Registerables.*;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class PapiBot {
    @Getter private Injector injector;
    @Getter private JDA jda;
    private static final Reflections REFLECTIONS = new Reflections("at.helpch.papibot");
    private static final BlockingQueue<GRunnable> QUEUE = new LinkedBlockingQueue<>();

    @Inject private GFile gFile;
    @Inject private MySQLInitializer mySQLInitializer;

    @Inject private EventHandler eventHandler;
    @Inject private CommandHandler commandHandler;

    void start(Injector injector) throws Exception {
        this.injector = injector;

        Stream.of(
                FILES, EVENTS, COMMANDS, BOT, MYSQL, CONSOLE
        ).forEach(PapiBot.this::register);

        //noinspection InfiniteLoopStatement
        while (true) QUEUE.take().run();
    }

    /**
     * Initialize individual components (registerables) in a controllable; clean way.
     * @param registerable The registerable to be registered.
     */
    public void register(Registerables registerable) {
        switch (registerable) {
            case FILES:
                Stream.of("config.json", "schema.sql").forEach(i -> gFile.make(i, "./" + i, "/" + i));
                break;

            case EVENTS:
                REFLECTIONS.getSubTypesOf(GEvent.class).stream().map(injector::getInstance).forEach(eventHandler.getEvents()::add);
                break;

            case COMMANDS:
                REFLECTIONS.getSubTypesOf(Command.class).stream().map(injector::getInstance).forEach(commandHandler.getCommands()::add);
                break;

            case BOT:
                try {
                    jda = new JDABuilder(gFile.getFileConfiguration("config").getString("token"))
                            .setGame(Game.of(Game.GameType.WATCHING, "the eCloud"))
                            .addEventListener(eventHandler)
                            .build();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case MYSQL:
                mySQLInitializer.connect();
                break;

            case CONSOLE:
                Task.async(r -> {
                    Scanner input = new Scanner(System.in);

                    while (true) {
                        switch (input.nextLine().toLowerCase()) {
                            case "stop": System.exit(0); break;
                        }
                    }
                }, "Console Command Monitor");

                break;

        }
    }

    /**
     * Queue a GRunnable to run on the main thread.
     * @param gRunnable GRunnable instance.
     */
    public void queue(GRunnable gRunnable) {
        QUEUE.add(gRunnable);
    }
}
