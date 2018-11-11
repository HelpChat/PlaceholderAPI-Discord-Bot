package at.helpch.papibot.commands;

import at.helpch.papibot.core.framework.Command;
import at.helpch.papibot.core.handlers.misc.PaginationHandler;
import at.helpch.papibot.core.objects.paginations.PaginationBuilder;
import at.helpch.papibot.core.objects.paginations.PaginationPage;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.inject.Inject;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ListCommand extends Command {
    @Inject private Gson gson;
    @Inject private PaginationHandler paginationHandler;

    public ListCommand() {
        super("list");
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("https://api.extendedclip.com/");
        List<String> keys = new ArrayList<>();

        try {
            HttpResponse response = client.execute(get);
            keys.addAll(gson.fromJson(EntityUtils.toString(response.getEntity()), LinkedTreeMap.class).keySet());
        } catch (Exception ex) {
            e.getChannel().sendMessage(e.getAuthor().getAsMention() + "The eCloud is currently non responsive, please report this to staff in https://helpch.at/discord").queue();
        }

        keys = Arrays.asList(String.join("\n", keys).replaceAll("((.*\\s*\\n\\s*){25})", "$1-SEPARATOR-\n").split("-SEPARATOR-"));
        String[] title = new String[] {"Expansions in the eCloud", "https://api.extendedclip.com/all"};

        if (keys.size() == 1) {
            e.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle(title[0], title[1])
                    .addField("Expansions: ", keys.get(0), false)
                    .setTimestamp(ZonedDateTime.now())
                    .build()).queue(s -> s.delete().queueAfter(5, TimeUnit.MINUTES));
        } else {
            List<String> unicodes = new ArrayList<>();

            Stream.of(
                    "1\u20E3", "2\u20E3", "3\u20E3", "4\u20E3", "5\u20E3", "6\u20E3", "7\u20E3", "8\u20E3", "9\u20E3"
            ).forEach(unicodes::add);

            AtomicInteger i = new AtomicInteger(0);
            PaginationBuilder paginationBuilder = new PaginationBuilder();

            keys.forEach(someKeys -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(title[0], title[1])
                        .setTimestamp(ZonedDateTime.now());

                PaginationPage page = new PaginationPage(embed.addField(new MessageEmbed.Field("Placeholders:", someKeys, false)).build(), unicodes.get(i.getAndIncrement()));
                paginationBuilder.addPages(page);
            });

            paginationBuilder.build(e.getChannel(), paginationHandler);
        }
    }
}
