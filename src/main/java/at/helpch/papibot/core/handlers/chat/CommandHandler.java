package at.helpch.papibot.core.handlers.chat;

import at.helpch.papibot.commands.HelpCommand;
import at.helpch.papibot.core.framework.Command;
import at.helpch.papibot.core.handlers.GEvent;
import at.helpch.papibot.core.objects.enums.EventsEnum;
import at.helpch.papibot.core.utils.mysql.ServerUtils;
import at.helpch.papibot.core.utils.string.StringUtils;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Getter;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class CommandHandler extends GEvent {
    @Inject private HelpCommand helpCommand;

    @Getter private final List<Command> commands = new ArrayList<>();

    public CommandHandler() {
        super(EventsEnum.MESSAGE_CREATE);
    }

    @Override
    protected void execute(Event event) {
        GuildMessageReceivedEvent e = (GuildMessageReceivedEvent) event;
        String msg = e.getMessage().getContentRaw();
        String prefix = ServerUtils.getPrefix(e.getGuild().getIdLong());
        boolean success = false;

        if (StringUtils.startsWith(msg, new String[]{prefix})) {
            if (msg.equalsIgnoreCase(prefix)) {
                helpCommand.run(e, null);
                e.getMessage().delete().queue();
                return;
            }

            for (Command cmd : commands) {
                String[] command = Arrays.stream(cmd.getCommand()).map(i -> prefix + " " + i).toArray(String[]::new);

                if (StringUtils.startsWith(msg, command) || command[0].contains("*")) {
                    String[] args = msg.toLowerCase().replace(prefix, "").trim().split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                    args = command[0].contains("*") ? args : args.length >= 1 ? Arrays.copyOfRange(args, 1, args.length) : args;
                    cmd.run(e, args);
                    success = true;
                }
            }

            if (!success) {
                e.getChannel().sendMessage(e.getAuthor().getAsMention() + " Unknown command, type `?papi help` for help.").queue(s -> s.delete().queueAfter(15, TimeUnit.SECONDS));
            }

            e.getMessage().delete().queue();
        }
    }
}
