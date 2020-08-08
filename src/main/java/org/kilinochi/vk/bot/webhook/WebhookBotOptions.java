package org.kilinochi.vk.bot.webhook;

import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class WebhookBotOptions extends VKBotOptions {

    public static final WebhookBotOptions DEFAULT = new WebhookBotOptions(null);

    private boolean shouldRemoveOldSubscriptions;
    private boolean shouldRemoveSubscriptionOnStop;

    public WebhookBotOptions(@Nullable Set<String> updateTypes) {
        super(updateTypes);
        shouldRemoveOldSubscriptions = true;
    }

    public boolean shouldRemoveOldSubscriptions() {
        return shouldRemoveOldSubscriptions;
    }

    public void setShouldRemoveOldSubscriptions(boolean shouldRemoveOldSubscriptions) {
        this.shouldRemoveOldSubscriptions = shouldRemoveOldSubscriptions;
    }

    public boolean shouldRemoveSubscriptionOnStop() {
        return shouldRemoveSubscriptionOnStop;
    }

    public void setShouldRemoveSubscriptionOnStop(boolean shouldRemoveSubscriptionOnStop) {
        this.shouldRemoveSubscriptionOnStop = shouldRemoveSubscriptionOnStop;
    }
}
