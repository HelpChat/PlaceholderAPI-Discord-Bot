package at.helpch.papibot.core.handlers.chat;

import at.helpch.papibot.core.framework.Command;
import at.helpch.papibot.core.handlers.GEvent;
import at.helpch.papibot.core.objects.enums.EventsEnum;
import at.helpch.papibot.core.utils.string.StringUtils;
import com.google.inject.Singleton;
import lombok.Getter;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class CommandHandler extends GEvent {
    @Getter private static final List<Command> COMMANDS = new ArrayList<>();

    public CommandHandler() {
        super(EventsEnum.MESSAGE_CREATE);
    }

    @Override
    protected void execute(Event event) {
        GuildMessageReceivedEvent e = (GuildMessageReceivedEvent) event;
        String msg = e.getMessage().getContentRaw();

        for (Command cmd : COMMANDS) {
            String command =

            if (StringUtils.startsWith(msg, cmd.getCommand())) {
                cmd.run(e, msg.toLowerCase().replaceFirst(badArgs[0].toLowerCase(), "").trim().split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
            }
        }
    }
}
