package net.purefunc.transmit.sdk

//import net.purefunc.core.domain.data.tryOrFailure
//import net.purefunc.transmit.external.EmailClient
//import java.util.Properties
//import javax.mail.Authenticator
//import javax.mail.Message
//import javax.mail.PasswordAuthentication
//import javax.mail.Session
//import javax.mail.Transport
//import javax.mail.internet.InternetAddress
//import javax.mail.internet.MimeMessage
//
//class GmailClient(
//    private val userName: String,
//    private val password: String,
//) : EmailClient {
//
//    private val properties = Properties()
//
//    init {
//        properties["mail.smtp.host"] = "smtp.gmail.com"
//        properties["mail.smtp.port"] = "587"
//        properties["mail.smtp.auth"] = "true"
//        properties["mail.smtp.starttls.enable"] = "true"
//        properties["mail.smtp.ssl.protocols"] = "TLSv1.2"
//    }
//
//    override fun send(subject: String, personal: String, address: String, htmlContent: String) = tryOrFailure {
//        Transport.send(createMessage(createSession(), personal, address, subject, htmlContent))
//    }
//
//    private fun createMessage(
//        session: Session,
//        personal: String,
//        address: String,
//        subject: String,
//        htmlContent: String
//    ) = MimeMessage(session).apply {
//        this.setFrom(InternetAddress(userName, personal))
//        this.setRecipients(Message.RecipientType.TO, arrayOf(InternetAddress(address)))
//        this.subject = subject
//        this.setContent(htmlContent, "text/html; charset=UTF-8")
//    }
//
//    private fun createSession() = Session.getInstance(properties,
//        object : Authenticator() {
//            override fun getPasswordAuthentication(): PasswordAuthentication {
//                return PasswordAuthentication(userName, password)
//            }
//        })
//}