package org.kilinochi.vk.bot.service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.CallbackServer;
import com.vk.api.sdk.objects.groups.responses.AddCallbackServerResponse;
import com.vk.api.sdk.objects.groups.responses.GetCallbackServersResponse;
import com.vk.api.sdk.queries.groups.GroupsAddCallbackServerQuery;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.kilinochi.vk.bot.exeption.VKBotException;
import org.kilinochi.vk.bot.webhook.WebhookBotContainer;
import org.kilinochi.vk.bot.webhook.WebhookBotOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author arman.shamenov
 */
public abstract class WebhookBot implements VKBot {
    protected static final Logger logger = LoggerFactory.getLogger(WebhookBot.class);

    private final WebhookBotOptions options;
    private final AtomicBoolean running = new AtomicBoolean();

    protected final VkApiClient client;
    protected final GroupActor groupActor;
    protected final String botName;

    public WebhookBot(WebhookBotOptions options,
                      VkApiClient client,
                      GroupActor groupActor,
                      String botName) {
        this.options = options;
        this.client = client;
        this.groupActor = groupActor;
        this.botName = botName;
    }

    public WebhookBot(VkApiClient client,
                      GroupActor groupActor,
                      String botName) {
        this.groupActor = groupActor;
        this.botName = botName;
        this.options = WebhookBotOptions.DEFAULT;
        this.client = client;
    }

    @Override
    public VkApiClient getClient() {
        return client;
    }

    @Override
    public String getName() {
        return botName;
    }

    public boolean stop(WebhookBotContainer container) {
        container.unregister(this);
        return running.compareAndSet(true, false);
    }

    public boolean isRunning() {
        return running.get();
    }

    public String getToken() {
        return groupActor.getAccessToken();
    }

    public boolean start(WebhookBotContainer container) throws VKBotException {
        if (!running.compareAndSet(false, true)) {
            return false;
        }

        if (options.shouldRemoveOldSubscriptions()) {
            try {
                unsubscribeAllInGroup();
            } catch (ApiException | ClientException e) {
                logger.error("Failed to remove current subscriptions ", e);
            }
        }

        try {
            subscribe(container.getWebhookUrl(this));
        } catch (ApiException | ClientException e) {
            throw new VKBotException("Failed to start webhook bot ", e);
        }

        return true;
    }

    protected void subscribe(String webhookUrl) throws ApiException, ClientException {

        GroupsAddCallbackServerQuery query = client.groups().addCallbackServer(
                groupActor,
                groupActor.getGroupId(),
                webhookUrl,
                getName());


        AddCallbackServerResponse response = query.execute();
        Integer serverId = response.getServerId();
        logger.info("Bot {} registered on webhook URL: {} and serverId = {}", getName(), webhookUrl, serverId);
    }

    protected void unsubscribeAllInGroup() throws ClientException, ApiException {
        GetCallbackServersResponse response = client.groups().getCallbackServers(groupActor, groupActor.getGroupId()).execute();
        List<CallbackServer> servers = response.getItems();
        for (CallbackServer server : servers) {
            Integer serverId = server.getId();
            client.groups().deleteCallbackServer(groupActor, groupActor.getGroupId(), serverId).execute();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("options", options)
                .append("running", running)
                .append("client", client)
                .append("groupActor", groupActor)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WebhookBot that = (WebhookBot) o;

        return new EqualsBuilder()
                .append(options, that.options)
                .append(running, that.running)
                .append(client, that.client)
                .append(groupActor, that.groupActor)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(options)
                .append(running)
                .append(client)
                .append(groupActor)
                .toHashCode();
    }
}
