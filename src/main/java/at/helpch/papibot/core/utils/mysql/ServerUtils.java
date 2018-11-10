package at.helpch.papibot.core.utils.mysql;

import at.helpch.papibot.core.objects.tasks.Task;
import co.aikar.idb.DB;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class ServerUtils {
    public static String getPrefix(long guildId) {
        String prefix;

        try {
            prefix = DB.getFirstRowAsync("SELECT * FROM `papibot_server` WHERE `guild_id`=?;", guildId).get().getString("prefix");
        } catch (Exception ignored) {
            prefix = "?papi";
        }

        return prefix;
    }

    public static void setPrefix(long guildId, String prefix) {
        try {
            if (DB.getFirstRowAsync("SELECT * FROM `papibot_server` WHERE `guild_id`=?;", guildId).get() != null) {
                DB.executeUpdateAsync("UPDATE `papibot_server` SET `prefix`=? WHERE `guild_id`=?;", guildId);
            } else {
                Task.async(r -> {
                    try {
                        DB.executeInsert("INSERT INTO `papibot_servers` (`id`, `guild_id`, `prefix`) VALUES ('0', ?, ?);", guildId, prefix);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}