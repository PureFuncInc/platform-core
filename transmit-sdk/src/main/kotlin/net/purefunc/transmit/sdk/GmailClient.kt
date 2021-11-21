package net.purefunc.transmit.sdk

import net.purefunc.common.domain.data.Failure
import net.purefunc.common.domain.data.Success
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

    init {
        properties["mail.smtp.host"] = "smtp.gmail.com";
        properties["mail.smtp.port"] = "587";
        properties["mail.smtp.auth"] = "true";
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.ssl.protocols"] = "TLSv1.2"
    }

    override fun send(subject: String, address: String, htmlContent: String) =
        run {
            try {
                Session.getInstance(properties,
                    object : Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(userName, password)
                        }
                    })
            } catch (ex: Exception) {
                Failure("", "")
            }
        }.run {
            val mimeMessage = MimeMessage(this as Session)
            mimeMessage.setFrom(InternetAddress(userName, "\$_ purefunc"))
            mimeMessage.setRecipients(Message.RecipientType.TO, arrayOf(InternetAddress(address)))
            mimeMessage.subject = subject
            mimeMessage.setContent(htmlContent, "text/html; charset=UTF-8")

            try {
                Transport.send(mimeMessage)
                Success("Success !!!")
            } catch (ex: Exception) {
                Failure("", "")
            }
        }
}