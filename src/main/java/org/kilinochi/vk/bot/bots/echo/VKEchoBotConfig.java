package org.kilinochi.vk.bot.bots.echo;

public class VKEchoBotConfig {
    private String echoBotToken;
    private int groupId;

    public VKEchoBotConfig() {

    }

    public int getGroupId() {
        return groupId;
    }

    public String getEchoBotToken() {
        return echoBotToken;
    }

    public void setEchoBotToken(String echoBotToken) {
        this.echoBotToken = echoBotToken;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
