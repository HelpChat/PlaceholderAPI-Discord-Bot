package at.helpch.papibot.core.objects;

import at.helpch.papibot.core.storage.file.FileConfiguration;
import at.helpch.papibot.core.utils.string.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PapiExpansion {
    private final Gson gson;
    private FileConfiguration json;
    @Getter private SuccessTypes success = SuccessTypes.SUCCESS;

    public PapiExpansion(Gson gson) {
        this.gson = gson;
    }

    public PapiExpansion load(String expansionName) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("https://api.extendedclip.com/v3/?name=" + expansionName);

        try {
            HttpResponse response = client.execute(get);
            List<Map<String, Object>> result = gson.fromJson(EntityUtils.toString(response.getEntity()), new TypeToken<List<Map<String, Object>>>(){}.getType());
            json = new FileConfiguration(result.get(0));
        } catch (IOException e) {
            success = SuccessTypes.ECLOUD_DOWN;
            return this;
        } catch (Exception e) {
            success = SuccessTypes.UNKNOWN_EXPANSION;
        }

        return this;
    }

    public String getAuthor() {
        return json.getString("author", "clip");
    }

    public List<String> getPlaceholders() {
        return Arrays.asList(String.join("\n", StringUtils.replaceAll(json.getStringList("placeholders"), "_", "\\_")).replaceAll("((.*\\s*\\n\\s*){15})", "$1-SEPARATOR-\n").split("-SEPARATOR-"));
    }

    public String getVersion() {
        return json.getString("latest_version", "1.0");
    }

    public enum SuccessTypes {
        SUCCESS,
        UNKNOWN_EXPANSION,
        ECLOUD_DOWN
    }
}
