package org.kilinochi.vk.bot.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.kilinochi.vk.bot.bots.echo.VKEchoBotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

@Configuration
public class AppConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(AppConfiguration.class);

    @Value("${server.port}")
    private int port;

    @Value("${ngrok.url}")
    private String ngrokUrl;

    @Bean
    public Gson gson() {
        return new GsonBuilder().create();
    }

    @Bean
    public VkApiClient client(Gson gson) {
        TransportClient transportClient = new HttpTransportClient();
        return new VkApiClient(transportClient, gson, 5);
    }

    @Bean
    @ConfigurationProperties(prefix = "vk")
    public VKEchoBotConfig vkEchoBotConfig() {
        return new VKEchoBotConfig();
    }

    @Bean
    public NgrokTunnel ngrokTunnel() throws UnirestException {
        NgrokTunnel ngrokTunnel = new NgrokTunnel(port);
        logger.info("ngrok nunnel on url {}", ngrokTunnel.url());
        return ngrokTunnel;
    }
}
