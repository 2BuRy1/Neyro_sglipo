package com.neyro.sglipa.project.configurations

import com.neyro.sglipa.project.services.BotService
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Component
class BotInitializer {



    @Bean
    fun register(bot: BotService): TelegramBotsApi {
        var defaultBotSession = DefaultBotSession()
        var telegramBotsApi = TelegramBotsApi(defaultBotSession.javaClass)
        telegramBotsApi.registerBot(bot)
        return  telegramBotsApi
    }


}