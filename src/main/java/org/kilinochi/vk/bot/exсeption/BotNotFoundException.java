package org.kilinochi.vk.bot.exсeption;

/**
 * @author arman.shamenov
 */
public class BotNotFoundException extends WebhookException {
    public BotNotFoundException(String message) {
        super(404, message);
    }
}
