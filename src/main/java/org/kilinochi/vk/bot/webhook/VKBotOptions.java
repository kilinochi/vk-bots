package org.kilinochi.vk.bot.webhook;

import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class VKBotOptions {
    private final Set<String> updateTypes;

    public VKBotOptions(@Nullable Set<String> updateTypes) {
        this.updateTypes = updateTypes;
    }

    public Set<String> getUpdateTypes() {
        return updateTypes;
    }
}
