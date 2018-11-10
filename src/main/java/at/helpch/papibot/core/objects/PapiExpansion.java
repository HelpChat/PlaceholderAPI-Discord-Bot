package at.helpch.papibot.core.objects;

import at.helpch.papibot.core.storage.file.FileConfiguration;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.Map;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class PapiExpansion {
    private final Gson gson;
    private FileConfiguration json;

    public PapiExpansion(Gson gson) {
        this.gson = gson;
    }

    @SuppressWarnings("unchecked")
    public PapiExpansion load(String expansionName) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("https://api.extendedclip.com/v3/?name=" + expansionName);

        try {
            HttpResponse response = client.execute(get);
            Map<String, Object> itemMap = gson.fromJson(EntityUtils.toString(response.getEntity()), LinkedTreeMap.class);
            json = new FileConfiguration((Map<String, Object>) itemMap.get(itemMap.keySet().toArray(new String[]{})[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public String getAuthor() {
        return json.getString("author", "clip");
    }

    public String getPlaceholders() {
        return String.join(" ", json.getStringList("placeholders"));
    }

    public String getVersion() {
        return json.getString("latest_version", "1.0");
    }
}
