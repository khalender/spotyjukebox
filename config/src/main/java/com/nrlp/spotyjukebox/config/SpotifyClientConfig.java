package com.nrlp.spotyjukebox.config;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Base64;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nrlp.spotyjukebox.config.SpotifyClientConfig.TokenResponse;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
// @Slf4j
public class SpotifyClientConfig {

    private final ObjectMapper objectMapper;
    Log log = LogFactory.getLog(SpotifyClientConfig.class);
    
    private static final String CLIENT_ID = "fcdc1b9cb87a4545834244006a136b62";
    private static final String CLIENT_SECRET = "a4f84c47dd5a499d9e4f7bf91f7a9046";
    private static final String CREDENTIALS_BASE_STRING = CLIENT_ID + ":" + CLIENT_SECRET;
    private static final String AUTH_HEADER_V_STRING = "Basic " + Base64.getEncoder()
    .encodeToString(CREDENTIALS_BASE_STRING.getBytes());
    
    String tokenUrl = "https://accounts.spotify.com/api/token";

    public String getToken(HttpClient httpClient) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenUrl))
                .header("Authorization", "Basic " + AUTH_HEADER_V_STRING)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                .build();

        try {
            // Send the request and get the response
            TokenResponse response = httpClient
                    .sendAsync(request, BodyHandlers.ofString())
                    .thenApplyAsync(res -> {
                        if (res.statusCode() != 200) {
                            throw new RuntimeException("Error: " + res.statusCode());
                        }
                        return res;
                    })
                    .thenApply(HttpResponse::body)
                    .thenApply(str -> parseTokenResponseQuietely(str))
                    .join();

            return response.accessToken();

            // Check the response status code
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }

    // create a new http client
    @Bean
    HttpClient httpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(30))
                .build();
    }

// create a new http client
    @Bean
    <T> HttpRequest httpRequest(String uri, String method, T body) throws URISyntaxException, IOException, InterruptedException {
        var token = getToken(httpClient());

        return HttpRequest.newBuilder()
                .uri(new URI("https://api.spotify.com/v1/search?q=abba&type=track"))
                .header("Authorization", "Bearer " + token)
                .build();
    }

    record TokenResponse(String accessToken, String tokenType, int expiresIn) {

    }

    record ErrorResponse(String error, String errorDescription) {

    }

    TokenResponse parseTokenResponseQuietely(String json) {
        TokenResponse response = null;
        try {
            response = objectMapper.readValue(json, TokenResponse.class);
        } catch (JsonProcessingException e) {
            log.info( "Error in parsing TokenResponse " + e.getMessage());
        }
        return response;
    }
}
