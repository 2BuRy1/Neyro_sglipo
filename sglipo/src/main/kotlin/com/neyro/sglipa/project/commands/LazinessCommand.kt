package com.neyro.sglipa.project.commands

import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.DefaultAbsSender
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import kotlin.random.Random
@Component
class LazinessCommand(@Autowired private val lazinessContainer: LazinessContainer) : Command("/set", "set level of laziness in process") {
    override fun sendMessage(absSender: DefaultAbsSender, update: Update) {

        val percentage = update.message.text.split(" ")[1].toInt()
        print("perc: ${percentage}   ${update.message.text}")

        val send = SendMessage()



        lazinessContainer.addLaziness(update.message.chatId.toString(), percentage)

        send.text = "laziness set to ${percentage}%"
        send.chatId = update.message.chatId.toString()
        absSender.execute(send)


    }

}