package com.lukecheskin.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RebrickableClient {
    private static final String API_KEY = "7d7f9429ac873f669448e17ee5a6399e";
    private static final String SETS_URL = "https://rebrickable.com/api/v3/lego/sets/";
    private static final String THEMES_URL = "https://rebrickable.com/api/v3/lego/themes/";
    private final HttpClient httpClient;

    public RebrickableClient() {
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Fetches information about a LEGO set from the Rebrickable API
     * @param setNumber The set number to look up
     * @return JsonObject containing set information including name, pieces, and theme_id
     */
    public JsonObject getSetInfo(String setNumber) throws IOException, InterruptedException {
        // API requires "-1" suffix
        String apiSetNumber = setNumber + "-1";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SETS_URL + apiSetNumber + "/?key=" + API_KEY))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        return JsonParser.parseString(response.body()).getAsJsonObject();
    }

    /**
     * Fetches theme information from the Rebrickable API
     * @param themeId The theme ID to look up
     * @return JsonObject containing theme information including name
     */
    public JsonObject getThemeInfo(int themeId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(THEMES_URL + themeId + "/?key=" + API_KEY))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        return JsonParser.parseString(response.body()).getAsJsonObject();
    }
}