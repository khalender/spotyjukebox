package com.nrlp.spotyjukebox.config;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.Base64;
/*
  @author Mustafa Nrlp
 */

public class SpotifyTokenProvider {
    private static final String SPOTIFY_TOKEN_URL = "https://api.spotify.com/v1/token";
    private static final String CLIENT_ID = "your_client_id";
    private static final String CLIENT_SECRET = "your_client_secret";

    public String getAccessToken() {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SPOTIFY_TOKEN_URL))
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RuntimeException("Failed to retrieve access token. Status code: " + response.statusCode());
            }
        } catch (HttpTimeoutException e) {
            throw new RuntimeException("Timeout while retrieving access token", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve access token", e);
        }
    }
}
