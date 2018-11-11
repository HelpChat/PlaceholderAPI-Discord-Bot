package at.helpch.papibot.commands;

import at.helpch.papibot.core.framework.Command;
import at.helpch.papibot.core.utils.mysql.ServerUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PrefixCommand extends Command {
    public PrefixCommand() {
        super("prefix");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (e.getMember() == e.getGuild().getOwner()) {
            if (args.length >= 1) {
                ServerUtils.setPrefix(e.getGuild().getIdLong(), args[0]);
                e.getChannel().sendMessage(e.getAuthor().getAsMention() + " :white_check_mark: **Prefix successfully set to `" + args[0] + "`.**").queue(s -> s.delete().queueAfter(15, TimeUnit.SECONDS));
            } else {
                e.getChannel().sendMessage(e.getAuthor().getAsMention() + " :warning: **Please supply the prefix you want to set.**").queue(s -> s.delete().queueAfter(30, TimeUnit.SECONDS));
            }
        } else {
            e.getChannel().sendMessage(e.getAuthor().getAsMention() + " :warning: **Only the server owner can use this command.**").queue(s -> s.delete().queueAfter(30, TimeUnit.SECONDS));
        }
    }
}
