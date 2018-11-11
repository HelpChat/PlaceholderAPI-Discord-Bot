package at.helpch.papibot.commands;

import at.helpch.papibot.core.framework.Command;
import at.helpch.papibot.core.utils.mysql.ServerUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class HelpCommand extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        String prefix = "**" + ServerUtils.getPrefix(e.getGuild().getIdLong()) + " ";

        e.getChannel().sendMessage(new EmbedBuilder()
                .setTitle("PlaceholderAPI Bot Help Menu", "https://github.com/help-chat/PlaceholderAPI-Discord-Bot")
                .setThumbnail(e.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .setDescription(
                        prefix + "help** - This menu.\n" +
                        prefix + "list** - Expansion list.\n" +
                        prefix + "<expansion name>** - Get placeholders for a specific expansion.\n" +
                        prefix + "status** - Get the current status of the eCloud.\n" +
                        prefix + "prefix <prefix>** - Admin command to change the bot's prefix for this specific guild.")
                .addField("Support: ", "Having trouble with PlaceholderAPI or any other plugin? Don't hesitate to ask for help in our help chat, https://helpch.at/discord", false)
                .setTimestamp(ZonedDateTime.now())
                .build()).queue(s -> s.delete().queueAfter(1, TimeUnit.MINUTES));
    }
}
