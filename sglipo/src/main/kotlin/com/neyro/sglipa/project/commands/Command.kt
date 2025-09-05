package com.neyro.sglipa.project.commands

import org.telegram.telegrambots.bots.DefaultAbsSender
import org.telegram.telegrambots.meta.api.objects.Update

abstract class Command(val name: String, val description: String) {

    abstract fun sendMessage(absSender: DefaultAbsSender, update: Update)

}