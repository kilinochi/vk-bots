package org.kilinochi.vk.bot;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.kilinochi.vk.bot.service.BotRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

import java.io.IOException;

@SpringBootApplication
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);
        BotRegistry botRegistry = context.getBean(BotRegistry.class);
        botRegistry.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NgrokTunnel ngrokTunnel = context.getBean(NgrokTunnel.class);
            try {
                ngrokTunnel.close();
            } catch (IOException | UnirestException e) {
                logger.error("Error while close ngrok tunnel : ", e);
            }
        }));
    }
}
