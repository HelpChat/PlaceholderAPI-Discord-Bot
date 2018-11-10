package at.helpch.papibot.core.handlers;

import at.helpch.papibot.core.objects.enums.EventsEnum;
import at.helpch.papibot.core.objects.tasks.Task;
import com.google.inject.Singleton;
import lombok.Getter;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.EventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class EventHandler implements EventListener {
    @Getter private static final List<GEvent> events = new ArrayList<>();

    @Override
    public void onEvent(Event event) {
        if (EventsEnum.contains(event)) {
            events.forEach(e -> {
                if (Arrays.asList(e.getEvents()).contains(EventsEnum.fromEvent(event))) {
                    Task.async(r -> e.run(event, r));
                }
            });
        }
    }
}
