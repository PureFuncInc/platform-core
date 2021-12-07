package net.purefunc.transmit.application.impl
//
//import io.mockk.every
//import io.mockk.mockk
//import net.purefunc.core.domain.data.Failure
//import net.purefunc.core.domain.data.Success
//import net.purefunc.transmit.external.EmailClient
//import net.purefunc.transmit.external.VoiceClient
//import net.purefunc.transmit.external.SmsClient
//import org.assertj.core.api.Assertions
//import org.junit.jupiter.api.Test
//
//class TransmitServiceImplTests {
//
//    private val emailClient = mockk<EmailClient>()
//    private val smsClient = mockk<SmsClient>()
//    private val phoneCallClient = mockk<VoiceClient>()
//
//    private val transmitService = TransmitServiceImpl(emailClient, smsClient, phoneCallClient)
//
//    @Test
//    internal fun `test send email success`() {
//        val subject = "Hi there, this is \$_ purefunc"
//        val personal = "\$_ purefunc"
//        val address = "yfr.huang@hotmail.com"
//        val httpContent = "<h1>Hello World !!</h1>"
//        every { emailClient.send(subject, personal, address, httpContent) } returns Success(Unit)
//
//        val result = transmitService.sendEmail(subject, personal, address, httpContent)
//
//        Assertions.assertThat(result is Success).isTrue
//    }
//
//    @Test
//    internal fun `test send mail failure because subject empty`() {
//        val subject = ""
//        val personal = "\$_ purefunc"
//        val address = "yfr.huang@hotmail.com"
//        val httpContent = "<h1>Hello World !!</h1>"
//        every { emailClient.send(subject, personal, address, httpContent) } returns Success(Unit)
//
//        val result = transmitService.sendEmail(subject, personal, address, httpContent)
//
//        Assertions.assertThat(result is Failure).isTrue
//    }
//
//    @Test
//    internal fun `test send mail failure because personal empty`() {
//        val subject = "Hi there, this is \$_ purefunc"
//        val personal = ""
//        val address = "yfr.huang@hotmail.com"
//        val httpContent = "<h1>Hello World !!</h1>"
//        every { emailClient.send(subject, personal, address, httpContent) } returns Success(Unit)
//
//        val result = transmitService.sendEmail(subject, personal, address, httpContent)
//
//        Assertions.assertThat(result is Failure).isTrue
//    }
//
//    @Test
//    internal fun `test send mail failure because invalid address`() {
//        val subject = "Hi there, this is \$_ purefunc"
//        val personal = "\$_ purefunc"
//        val address = "yfr.huanghotmail.com"
//        val httpContent = "<h1>Hello World !!</h1>"
//        every { emailClient.send(subject, personal, address, httpContent) } returns Success(Unit)
//
//        val result = transmitService.sendEmail(subject, personal, address, httpContent)
//
//        Assertions.assertThat(result is Failure).isTrue
//    }
//
//    @Test
//    internal fun `test send mail failure because http content empty`() {
//        val subject = "Hi there, this is \$_ purefunc"
//        val personal = "\$_ purefunc"
//        val address = "yfr.huang@hotmail.com"
//        val httpContent = ""
//        every { emailClient.send(subject, personal, address, httpContent) } returns Success(Unit)
//
//        val result = transmitService.sendEmail(subject, personal, address, httpContent)
//
//        Assertions.assertThat(result is Failure).isTrue
//    }
//}