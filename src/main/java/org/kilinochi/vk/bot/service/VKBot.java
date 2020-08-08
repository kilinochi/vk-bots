package org.kilinochi.vk.bot.service;

import com.vk.api.sdk.client.VkApiClient;

/**
 * @author arman.shamenov
 */
public interface VKBot {
    VkApiClient getClient();

    String onUpdate(String body);

    String getName();
}
