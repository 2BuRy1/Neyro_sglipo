package com.neyro.sglipa.project.services

import com.neyro.sglipa.project.commands.Command
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import java.util.*

@Service
class CommandResolver(@Autowired
                      private val applicationContext: ApplicationContext
) {

    fun resolveCommand(name: String): Optional<Command> {
        val commands = applicationContext.getBeansOfType(Command::class.java).values

        val command = commands.find { it.name == name }



        return Optional.ofNullable(command)
    }
}