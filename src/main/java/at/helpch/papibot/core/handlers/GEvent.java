package at.helpch.papibot.core.handlers;

import at.helpch.papibot.core.objects.enums.EventsEnum;
import at.helpch.papibot.core.objects.tasks.GRunnable;
import lombok.Getter;
import net.dv8tion.jda.core.events.Event;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public abstract class GEvent {
    @Getter private EventsEnum[] events;
    protected GRunnable instance;

    protected GEvent(EventsEnum... events) {
        this.events = events;
    }

    protected abstract void execute(Event event);

    public void run(Event event, GRunnable instance) {
        this.instance = instance;
        execute(event);
    }
}