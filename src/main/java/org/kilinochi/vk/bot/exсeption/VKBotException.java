package org.kilinochi.vk.bot.exсeption;

public class VKBotException extends Exception {
    public VKBotException(Throwable cause) {
        super(cause);
    }

    public VKBotException(String message, Exception cause) {
        super(message, cause);
    }
}
