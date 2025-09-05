package com.neyro.sglipa.project.commands

import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.stereotype.Component

class Laziness private constructor() {






    companion object {
        @Volatile private var instance: Laziness? = null

        fun getInstance(): Laziness {
            return instance ?: synchronized(this) {
                instance ?: Laziness().also { instance = it }
            }
        }
    }


}