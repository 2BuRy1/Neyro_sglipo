package com.neyro.sglipa.project.configurations

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class BotInfo {


    @Value("\${botToken}")
    lateinit var sglipoToken: String

    @Value("\${botName}")
    lateinit var sglipoName: String

}