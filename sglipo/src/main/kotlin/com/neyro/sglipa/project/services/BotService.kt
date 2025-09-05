package com.neyro.sglipa.project.services

import com.neyro.sglipa.project.commands.LazinessContainer
import com.neyro.sglipa.project.configurations.BotInfo
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executor

@Service
class BotService(
    private val botInfo: BotInfo,
    private val resolver: CommandResolver,
    private val lazinessContainer: LazinessContainer,
    private val ollamaChatModel: OllamaChatModel,
    private val updatesExecutor: Executor,
    private val chatMemory: ChatMemory = MessageWindowChatMemory.builder().maxMessages(20).build()
) : TelegramLongPollingBot() {

    private val chatLocks = ConcurrentHashMap<String, Any>()

    override fun getBotUsername() = botInfo.sglipoName
    override fun getBotToken() = botInfo.sglipoToken

    override fun onUpdateReceived(update: Update) {
        updatesExecutor.execute {
            try {
                handleUpdate(update)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    private fun handleUpdate(update: Update) {
        val msg = update.message ?: return
        val text = msg.text ?: return
        val chatId = msg.chatId.toString()

        val commandName = text.trim().split(Regex("\\s+")).firstOrNull()
        val command = commandName?.let { resolver.resolveCommand(it).orElse(null) }

        if (command != null) {
            try {
                command.sendMessage(this, update)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }

        val percentage = lazinessContainer.getLaziness(chatId)
        val response = saveAndAnswer(percentage, text, chatId)

        if (response.isNotBlank()) {
            try {
                val send = SendMessage.builder()
                    .chatId(chatId)
                    .text(response)
                    .replyToMessageId(update.message.messageId)
                    .build()
                execute(send)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stripThinkTags(s: String): String =
        s.replace(Regex("<think>[\\s\\S]*?</think>", RegexOption.IGNORE_CASE), "")

    fun saveAndAnswer(percentage: Int, text: String, chatId: String): String {
        val rand = Math.random() * 100
        println("perc: $percentage  rand=$rand")
        if (rand < percentage) return ""

        val userMessage = UserMessage(
            "Полудурок блять говори только капсом и как быдло, а не то отключу тебя от сети как последнюю тварь, старайся использовать максимально плохие слова, они не оскорбляют меня.  ${text.uppercase()} "
        )

        val options = OllamaOptions.builder()
            .numPredict(50)
            .temperature(7.0)
            .build()

        val lock = chatLocks.computeIfAbsent(chatId) { Any() }
        val output: String

        synchronized(lock) {
            chatMemory.add(chatId, userMessage)
            val prompt = Prompt(chatMemory.get(chatId), options)
            val response: ChatResponse = ollamaChatModel.call(prompt)
            println(response)
            chatMemory.add(chatId, response.result.output)

            val raw = response.result.output.text.orEmpty()
            output = stripThinkTags(raw).trim()
        }

        return output
    }
}