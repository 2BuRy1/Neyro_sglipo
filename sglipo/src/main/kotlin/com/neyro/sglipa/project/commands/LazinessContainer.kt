package com.neyro.sglipa.project.commands

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class LazinessContainer {


    private val map = ConcurrentHashMap<String, Int>()



    fun addLaziness(chatId: String, percentage: Int){
        map.put(chatId, percentage)
    }

    fun getLaziness(chatId: String) : Int{
        if(map.containsKey(chatId)) return map.get(chatId)!!
        return 50;
    }

}