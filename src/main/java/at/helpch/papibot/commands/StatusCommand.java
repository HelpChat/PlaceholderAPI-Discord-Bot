package at.helpch.papibot.commands;

import at.helpch.papibot.core.framework.Command;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class StatusCommand extends Command {
    public StatusCommand() {
        super("status");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("https://api.extendedclip.com/").openConnection();

            if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                e.getChannel().sendMessage(e.getAuthor().getAsMention() + ":warning: **The eCloud is currently non responsive, please report this to staff in https://helpch.at/discord**").queue(s -> s.delete().queueAfter(45, TimeUnit.SECONDS));
            } else {
                e.getChannel().sendMessage(e.getAuthor().getAsMention() + " :white_check_mark: **The eCloud is up and responsive.**").queue(s -> s.delete().queueAfter(15, TimeUnit.SECONDS));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
