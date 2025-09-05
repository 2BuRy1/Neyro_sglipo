package com.neyro.sglipa.project.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

@Configuration
class AsyncConfig {
    @Bean
    fun updatesExecutor(): Executor {
        val ex = ThreadPoolTaskExecutor()
        ex.corePoolSize = 8
        ex.maxPoolSize = 16
        ex.queueCapacity = 200
        ex.setThreadNamePrefix("tg-worker-")
        ex.setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())
        ex.initialize()
        return ex
    }
}