package at.helpch.papibot.commands;

import at.helpch.papibot.core.framework.Command;
import at.helpch.papibot.core.handlers.misc.PaginationHandler;
import at.helpch.papibot.core.objects.PapiExpansion;
import at.helpch.papibot.core.objects.paginations.PaginationBuilder;
import at.helpch.papibot.core.objects.paginations.PaginationPage;
import at.helpch.papibot.core.utils.string.StringUtils;
import com.google.gson.Gson;
import com.google.inject.Inject;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ExpansionCommand extends Command {
    @Inject private Gson gson;
    @Inject private PaginationHandler paginationHandler;

    public ExpansionCommand() {
        super("*");
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length >= 1) {
            if (!StringUtils.equalsIgnoreCase(args[0], new String[]{"list", "help", "prefix", "status"})) {
                PapiExpansion exp = new PapiExpansion(gson).load(args[0]);

                if (exp.isSuccess()) {
                    String title = "Placeholders for " + args[0];
                    String[] footer = new String[]{"Version " + exp.getVersion() + " by " + exp.getAuthor(), "https://cdn.discordapp.com/avatars/510750938672136193/e6120b4adf0c99226064d78094348592.webp"};
                    MessageEmbed.Field command = new MessageEmbed.Field("Command:", "```/papi ecloud download " + args[0] + "\n/papi reload```", false);
                    List<String> placeholders = exp.getPlaceholders();

                    if (placeholders.size() == 1) {
                        MessageEmbed message = new EmbedBuilder()
                                .setTitle(title, "https://api.extendedclip.com/expansions/" + args[0].toLowerCase())
                                .addField("Placeholders: ", exp.getPlaceholders().get(0) + "\n\u200C", false)
                                .addField(command)
                                .setFooter(footer[0], footer[1])
                                .setTimestamp(ZonedDateTime.now())
                                .build();

                        e.getChannel().sendMessage(message).queue(m -> m.delete().queueAfter(5, TimeUnit.MINUTES));
                    } else {
                        List<String> unicodes = new ArrayList<>();

                        Stream.of(
                                "1\u20E3", "2\u20E3", "3\u20E3", "4\u20E3", "5\u20E3", "6\u20E3", "7\u20E3", "8\u20E3", "9\u20E3"
                        ).forEach(unicodes::add);

                        AtomicInteger i = new AtomicInteger(0);
                        PaginationBuilder paginationBuilder = new PaginationBuilder();

                        placeholders.forEach(somePlaceholders -> {
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setTitle(title, "https://api.extendedclip.com/expansions/" + args[0].toLowerCase())
                                    .setFooter(footer[0], footer[1])
                                    .setTimestamp(ZonedDateTime.now());

                            PaginationPage page = new PaginationPage(embed.addField(new MessageEmbed.Field("Placeholders:", somePlaceholders + "\n\u200C", false)).addField(command).build(), unicodes.get(i.getAndIncrement()));
                            paginationBuilder.addPages(page);
                        });

                        paginationBuilder.build(e.getChannel(), paginationHandler);
                    }
                } else {
                    e.getChannel().sendMessage(e.getAuthor().getAsMention() + " There was an error when trying to process your request, either the eCloud is down or the expansion you requested does not exist.").queue(s -> s.delete().queueAfter(15, TimeUnit.SECONDS));
                }
            }
        }
    }
}
