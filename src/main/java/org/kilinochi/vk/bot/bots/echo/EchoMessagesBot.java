package org.kilinochi.vk.bot.bots.echo;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.responses.GetCallbackConfirmationCodeResponse;
import org.jetbrains.annotations.NotNull;
import org.kilinochi.vk.bot.service.WebhookBot;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EchoMessagesBot extends WebhookBot {


    public EchoMessagesBot(VkApiClient client,
                           VKEchoBotConfig config) {
        super(client, new GroupActor(config.getGroupId(), config.getEchoBotToken()), "EchoBot");
    }

    @Override
    public String onUpdate(@NotNull String body) {

        Gson gson = getClient().getGson();
        JsonObject jsonObject = gson.fromJson(body, JsonObject.class);
        JsonElement type = jsonObject.get("type");
        String typeStr = type.getAsString();
        if ("confirmation".equals(typeStr)) {
            try {
                GetCallbackConfirmationCodeResponse confirmationCodeResponse = client.groups()
                        .getCallbackConfirmationCode(groupActor, groupActor.getGroupId())
                        .execute();
                return confirmationCodeResponse.getCode();
            } catch (ApiException | ClientException e) {
                logger.error("Error while get confirm code from service : ", e);
                return "Api Error while confirmation webhook";
            }
        }
        if ("message_new".equals(typeStr)) {
            JsonObject messageObj = jsonObject.get("object").getAsJsonObject();

            int fromId = messageObj.get("user_id").getAsInt();
            String text = messageObj.get("body").getAsString();

            if (text == null || text.isEmpty()) {
                return "OK";
            }

            try {
                Random random = new Random(System.currentTimeMillis());
                int randomId = random.nextInt();
                getClient().messages()
                        .send(groupActor)
                        .randomId(randomId)
                        .message("You say : " + text)
                        .userId(fromId)
                        .execute();
            } catch (ApiException | ClientException e) {
                logger.error("Error while send message request : ", e);
                return "Api bad response " + e.getMessage();
            }
            return "OK";
        }

        return "OK";
    }
}
