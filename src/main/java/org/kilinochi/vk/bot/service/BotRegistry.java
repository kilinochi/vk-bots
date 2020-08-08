package org.kilinochi.vk.bot.service;

import org.kilinochi.vk.bot.controller.WebhookController;
import org.kilinochi.vk.bot.exсeption.VKBotException;
import org.kilinochi.vk.bot.webhook.WebhookBotContainerBase;
import org.springframework.stereotype.Component;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @author arman.shamenov
 */
@Component
public class BotRegistry extends WebhookBotContainerBase {

    //private static final String SERVER_URL = "https://0ec7b7cca7d3.ngrok.io";
    private final List<WebhookBot> bots;
    private final NgrokTunnel ngrokTunnel;

    public BotRegistry(List<WebhookBot> bots,
                       NgrokTunnel ngrokTunnel) {
        this.bots = bots;
        this.ngrokTunnel = ngrokTunnel;
    }

    @Override
    public void start() {
        for (WebhookBot bot : bots) {
            register(bot);
        }

        try {
            super.start();
        } catch (VKBotException e) {
            logger.error("Error while start bots container : ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @PreDestroy
    public void stop() {
        super.stop();
    }

    @Override
    public String getWebhookUrl(WebhookBot bot) {
        return ngrokTunnel.url() + getPath(bot);
    }

    @Override
    protected String getPath(WebhookBot bot) {
        return WebhookController.PATH + "?key=" + bot.getToken();
    }
}
