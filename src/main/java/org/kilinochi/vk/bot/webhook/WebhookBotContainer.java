package org.kilinochi.vk.bot.webhook;

import org.kilinochi.vk.bot.exeption.VKBotException;
import org.kilinochi.vk.bot.exeption.WebhookException;
import org.kilinochi.vk.bot.service.WebhookBot;

import java.io.IOException;

public interface WebhookBotContainer {

    void register(WebhookBot webhookBot);

    void unregister(WebhookBot webhookBot);

    Iterable<WebhookBot> getBots();

    void start() throws VKBotException;

    void stop();

    String getWebhookUrl(WebhookBot bot);

    String handleRequest(String path, String method, String body) throws WebhookException, IOException;
}
