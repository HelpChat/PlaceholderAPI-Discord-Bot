package at.helpch.papibot.commands;

import at.helpch.papibot.core.framework.Command;
import at.helpch.papibot.core.objects.PapiExpansion;
import com.google.gson.Gson;
import com.google.inject.Inject;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ExpansionCommand extends Command {
    @Inject private Gson gson;

    public ExpansionCommand() {
        super("*");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length >= 1) {
            if (!args[0].equalsIgnoreCase("list")) {
                e.getChannel().sendMessage(new PapiExpansion(gson).load(args[0]).getPlaceholders()).queue();
            }
        } else {
            e.getChannel().sendMessage("Please supply an expansion name.").queue();
        }
    }
}
