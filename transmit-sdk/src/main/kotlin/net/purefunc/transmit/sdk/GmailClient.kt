package net.purefunc.transmit.sdk

import net.purefunc.core.domain.data.Failure
import net.purefunc.core.domain.data.Success
import net.purefunc.core.domain.data.then
import net.purefunc.core.domain.data.tryOrFailure
import net.purefunc.transmit.external.EmailClient
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class GmailClient(
    private val userName: String,
    private val password: String,
) : EmailClient {

    private val properties = Properties()
    private val emailRegex = "^(.+)@(.+)$".toRegex()

    init {
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.port"] = "587"
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.ssl.protocols"] = "TLSv1.2"
    }

    override fun send(subject: String, personal: String, address: String, htmlContent: String) =
        validate(subject, personal, address, htmlContent).then {
            tryOrFailure {
                Session.getInstance(properties,
                    object : Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(userName, password)
                        }
                    })
                    .run {
                        val mimeMessage = MimeMessage(this)
                        mimeMessage.setFrom(InternetAddress(userName, personal))
                        mimeMessage.setRecipients(Message.RecipientType.TO, arrayOf(InternetAddress(address)))
                        mimeMessage.subject = subject
                        mimeMessage.setContent(htmlContent, "text/html; charset=UTF-8")
                        Transport.send(mimeMessage)
                    }
            }
        }

    private fun validate(subject: String, personal: String, address: String, htmlContent: String) =
        when {
            subject.isEmpty() -> Failure(RuntimeException("subject is empty"))
            personal.isEmpty() -> Failure(RuntimeException("personal is empty"))
            emailRegex.matches(address) -> Failure(RuntimeException("address is invalid"))
            htmlContent.isEmpty() -> Failure(RuntimeException("htmlContent is empty"))
            else -> Success("")
        }
}