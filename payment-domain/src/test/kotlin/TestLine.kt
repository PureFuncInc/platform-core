import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.HmacUtils
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import java.math.BigDecimal
import java.util.UUID

val objectMapper = ObjectMapper()

fun String.encrypt(key: String) =
    String(Base64.encodeBase64(HmacUtils.getHmacSha256(key.toByteArray()).doFinal(this.toByteArray())))

fun Any.toJson(): String = objectMapper.writeValueAsString(this)

fun main() {
    val channelId = "1656569296"
    val channelSecret = "121f6e34a5cb112dfe4038b773e10260"

    val redirectUrls = RedirectUrls(
        "https://github.com/20211026",
        "https://github.com/20211026"
    )
    val productForm = ProductForm("PEN-B-001", "Pen Brown", BigDecimal("10"), BigDecimal("1"))
    val productPackageForm = ProductPackageForm("1", BigDecimal("10"), listOf(productForm))
    val form = CheckoutPaymentRequestForm(
        BigDecimal("10"),
        "TWD",
        "MKSI_S_20180904_1000003",
        listOf(productPackageForm),
        redirectUrls
    )

    val requestUri = "/v3/payments/request"
    val nonce = UUID.randomUUID().toString()
    val signature = (channelSecret + requestUri + form.toJson() + nonce).encrypt(channelSecret)

    val webClient = WebClient.builder()
        .baseUrl("https://sandbox-api-pay.line.me")
        .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader("X-LINE-ChannelId", channelId)
        .defaultHeader("X-LINE-Authorization-Nonce", nonce)
        .defaultHeader("X-LINE-Authorization", signature)
        .build()

    val response = webClient.post()
        .uri(requestUri)
        .bodyValue(form)
        .retrieve()
        .bodyToMono(object : ParameterizedTypeReference<Map<*, *>>() {})
        .block()

    println(response.toString())
}