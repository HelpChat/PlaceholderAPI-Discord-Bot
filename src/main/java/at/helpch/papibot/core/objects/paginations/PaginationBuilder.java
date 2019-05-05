package at.helpch.papibot.core.objects.paginations;

import at.helpch.papibot.core.handlers.misc.PaginationHandler;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PaginationBuilder {
    @Getter private List<PaginationPage> pages = new ArrayList<>();

    /**
     * Add pages to the pagination.
     * @param pages Pages to be added.
     */
    public void addPages(PaginationPage... pages) {
        this.pages.addAll(Arrays.asList(pages));
    }

    /**
     * Build a pagination and post it in a channel.
     * @param channel The channel the pagination will be posted in.
     */
    public void build(TextChannel channel, PaginationHandler paginationHandler) {
        if (!pages.isEmpty()) {
            List<Object> emotes = new ArrayList<>();
            Object message_ = pages.get(0).getMessage();
            Message message = null;
            PaginationSet paginationSet = new PaginationSet().addPages(pages);

            pages.stream().map(PaginationPage::getEmote).forEach(emotes::add);

            if (message_ instanceof String) {
                message = channel.sendMessage((String) message_).complete();
            } else if (message_ instanceof MessageEmbed) {
                message = channel.sendMessage((MessageEmbed) message_).complete();
            }

            assert message != null;

            final Message finalMessage = message;
            emotes.forEach(emote -> {
                if (emote instanceof String) {
                    finalMessage.addReaction((String) emote).queue();
                } else if (emote instanceof Emote) {
                    finalMessage.addReaction((Emote) emote).queue();
                }
            });

            paginationHandler.getPaginations().put(finalMessage.getId(), paginationSet);
            message.delete().queueAfter(5, TimeUnit.MINUTES);
        }
    }
}
