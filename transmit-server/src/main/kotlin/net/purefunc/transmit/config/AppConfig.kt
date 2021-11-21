package net.purefunc.transmit.config

import net.purefunc.transmit.sdk.GmailClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun gmailClient() = GmailClient("", "")
}