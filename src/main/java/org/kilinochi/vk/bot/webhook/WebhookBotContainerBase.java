package org.kilinochi.vk.bot.webhook;

import org.kilinochi.vk.bot.exсeption.BotNotFoundException;
import org.kilinochi.vk.bot.exсeption.VKBotException;
import org.kilinochi.vk.bot.exсeption.WebhookException;
import org.kilinochi.vk.bot.service.VKBot;
import org.kilinochi.vk.bot.service.WebhookBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author arman.shamenov
 */
public abstract class WebhookBotContainerBase implements WebhookBotContainer {

    protected static final Logger logger = LoggerFactory.getLogger(WebhookBotContainerBase.class);

    private final Map<String, WebhookBot> bots = new ConcurrentHashMap<>();
    private final Map<VKBot, String> reverseLookup = new ConcurrentHashMap<>();

    @Override
    public void register(WebhookBot webhookBot) {
        String path = getPath(webhookBot);
        if (bots.putIfAbsent(path, webhookBot) != null) {
            throw new IllegalStateException("Bot " + webhookBot + " is already registered");
        }

        reverseLookup.put(webhookBot, path);
    }

    @Override
    public void unregister(WebhookBot webhookBot) {
        String currentPath = reverseLookup.get(webhookBot);
        if (currentPath == null) {
            throw new IllegalStateException("Bot " + webhookBot + " is not registered");
        }

        reverseLookup.remove(webhookBot, currentPath);
        bots.remove(currentPath, webhookBot);
    }

    @Override
    public Iterable<WebhookBot> getBots() {
        return bots.values();
    }

    @Override
    public void start() throws VKBotException {
        for (WebhookBot bot : bots.values()) {
            try {
                boolean isStarted = bot.start(this);
                if (!isStarted) {
                    continue;
                }

                logger.info("Bot {} started", bot.getName());
            } catch (Exception e) {
                logger.error("Failed to start bot {}", bot.getName(), e);
            }
        }
    }

    @Override
    public void stop() {
        for (WebhookBot bot : bots.values()) {
            try {
                boolean isStopped = bot.stop(this);
                if (!isStopped) {
                    continue;
                }

                logger.info("Bot {} stopped", bot.getName());
            } catch (Exception e) {
                logger.error("Failed to stop bot {}", bot, e);
            }
        }
    }

    @Override
    public String handleRequest(String path, String method, String body) throws WebhookException {
        if (!"POST".equals(method)) {
            return "OK";
        }

        WebhookBot bot = bots.get(path);
        if (bot == null) {
            throw new BotNotFoundException("No bot registered by path: " + path);
        }

        return bot.onUpdate(body);
    }

    protected abstract String getPath(WebhookBot bot);
}
