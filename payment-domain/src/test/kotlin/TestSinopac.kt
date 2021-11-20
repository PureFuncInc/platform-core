import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun String.aesEncrypt(key: String, iv: String) =
    let {
        Cipher.getInstance("AES/CBC/PKCS5PADDING")
    }.also {
        it.init(
            Cipher.ENCRYPT_MODE,
            SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), "AES"),
            IvParameterSpec(iv.toByteArray(StandardCharsets.UTF_8))
        )
    }.let {
        Hex.encodeHexString(it.doFinal(this.toByteArray(StandardCharsets.UTF_8)))
    }

fun String.aesDecrypt(key: String, iv: String) =
    let {
        Cipher.getInstance("AES/CBC/PKCS5PADDING")
    }.also {
        it.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), "AES"),
            IvParameterSpec(iv.toByteArray(StandardCharsets.UTF_8))
        )
    }.let {
        String(it.doFinal(Hex.decodeHex(this.toCharArray())))
    }

fun main() {
    val shopNo = ""

    val webClient = WebClient.builder()
        .baseUrl("https://sandbox.sinopac.com")
        .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .build()

    val nonceResponse = webClient.post()
        .uri("/QPay.WebAPI/api/Nonce")
        .bodyValue(mapOf("ShopNo" to shopNo))
        .retrieve()
        .bodyToMono(object : ParameterizedTypeReference<Map<*, *>>() {})
        .block()

    val nonce = nonceResponse["Nonce"].toString().toUpperCase()

    val a1 = BigInteger("", 16)
    val a2 = BigInteger("", 16)
    val b1 = BigInteger("", 16)
    val b2 = BigInteger("", 16)

    val hashId = "${a1.xor(a2).toString(16)}${b1.xor(b2).toString(16)}".toUpperCase()

    val iv = DigestUtils.sha256Hex(nonce).takeLast(16).toUpperCase()

    val param = ""
    val sign = DigestUtils.sha256Hex("${param}${nonce}${hashId}").toUpperCase()

    val body = ""
    val message = body.aesEncrypt(hashId, iv)

    val request = mapOf(
        "Version" to "1.0.0",
        "ShopNo" to shopNo,
        "APIService" to "OrderCreate",
        "Sign" to sign,
        "Nonce" to nonce,
        "Message" to message,
    )

    val orderResponse = webClient.post()
        .uri("/funBIZ/QPay.WebAPI/api/Order")
        .bodyValue(message)
        .retrieve()
        .bodyToMono(object : ParameterizedTypeReference<Map<*, *>>() {})
        .block()
}
