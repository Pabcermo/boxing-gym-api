package com.boxinggym.api.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration          // Le dice a Spring que aquí hay Beans que debe registrar
public class OpenAIConfig {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.url}")
    private String openAIUrl;

    @Bean                   // Spring crea este objeto y lo inyecta donde se necesite
    public WebClient openAIWebClient() {
        return WebClient.builder()
                .baseUrl(openAIUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)  // Auth de OpenAI
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}