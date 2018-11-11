package at.helpch.papibot.core.handlers.misc;

import at.helpch.papibot.core.handlers.GEvent;
import at.helpch.papibot.core.objects.enums.EventsEnum;
import at.helpch.papibot.core.utils.mysql.ServerUtils;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class MySQLHandler extends GEvent {
    public MySQLHandler() {
        super(EventsEnum.GUILD_JOIN, EventsEnum.GUILD_LEAVE);
    }

    @Override
    protected void execute(Event event) {
        switch (EventsEnum.fromEvent(event)) {
            case GUILD_JOIN:
                ServerUtils.addServer(((GuildJoinEvent) event).getGuild().getIdLong());
                break;

            case GUILD_LEAVE:
                ServerUtils.removeServer(((GuildLeaveEvent) event).getGuild().getIdLong());
                break;
        }
    }
}
