package net.purefunc.transmit.client.webflux

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.mockk
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WebFluxClientImplTests {

    private lateinit var mockWebServer: MockWebServer
    private val webClient = WebClient.create("http://localhost:8080")
    private val transmitClient = WebFluxClientImpl(webClient)
    private val objectMapper = ObjectMapper()

    @BeforeAll
    internal fun setUpAll() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    @AfterAll
    internal fun tearDownAll() {
        mockWebServer.shutdown()
    }

    @Test
    internal fun testSendEmail() {
        mockWebServer.url("/api/v1/transmit/email")
        mockWebServer.enqueue(
            MockResponse()
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200)
                .setBody(objectMapper.writeValueAsString(mapOf("code" to "c482b457-b667-486e-bfaa-766e8ee8294b")))
        )

        Assertions.assertThat(transmitClient.sendEmail("abc@xyz.com", "Hi, I'm Vincent"))
            .isEqualTo("c482b457-b667-486e-bfaa-766e8ee8294b")
    }

    @Test
    internal fun testSendSms() {
        mockWebServer.url("/api/v1/transmit/sms")
        mockWebServer.enqueue(
            MockResponse()
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200)
                .setBody(objectMapper.writeValueAsString(mapOf("code" to "6e4c43df-8809-496c-bc1f-ca20012e638c")))
        )

        Assertions.assertThat(transmitClient.sendSms("abc@xyz.com", "Hi, I'm Vincent"))
            .isEqualTo("6e4c43df-8809-496c-bc1f-ca20012e638c")
    }

    @Test
    internal fun testMakePhoneCall() {
        mockWebServer.url("/api/v1/transmit/phone")
        mockWebServer.enqueue(
            MockResponse()
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200)
                .setBody(objectMapper.writeValueAsString(mapOf("code" to "41a1f84f-ca52-47f1-b9ea-99d5c4d4ab2e")))
        )

        Assertions.assertThat(transmitClient.sendSms("abc@xyz.com", "Hi, I'm Vincent"))
            .isEqualTo("41a1f84f-ca52-47f1-b9ea-99d5c4d4ab2e")

    }
}