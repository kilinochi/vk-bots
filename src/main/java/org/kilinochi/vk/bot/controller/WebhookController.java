package org.kilinochi.vk.bot.controller;

import org.kilinochi.vk.bot.exсeption.WebhookException;
import org.kilinochi.vk.bot.service.BotRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(WebhookController.PATH)
public class WebhookController {
    public static final String PATH = "/api/webhook";

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    private final BotRegistry botRegistry;

    public WebhookController(BotRegistry botRegistry) {
        this.botRegistry = botRegistry;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> handle(@RequestParam(name = "key") String token, @RequestBody String jsonBody) {
        String fullPath = PATH + "?key=" + token;
        return Mono.fromSupplier(() -> {
            try {
                return botRegistry.handleRequest(fullPath, "POST", jsonBody);
            } catch (WebhookException e) {
                logger.error("Error while handle request : ", e);
                return "Webhook error";
            }
        });

    }
}
