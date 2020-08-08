#Webhook VK bots

1. Устанавливаем на рабочую машину ngrok (https://ngrok.com/)

    1.2 Поднимаем ngrok-proxy при помощи команды
    
    ```ngrok http 6000```
2. Поднимаем приложение командой 
    
    ```./gradlew build && java -jar build/libs/vk-bots-1.0-SNAPSHOT.jar```



