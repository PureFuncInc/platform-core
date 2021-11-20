package com.sysfun.member

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.mockk.every
import io.mockk.mockk
import net.funpodium.common.domain.data.type.Operation
import net.funpodium.member.application.impl.MemberServiceImpl
import net.funpodium.member.domain.data.entity.Member
import net.funpodium.member.domain.data.type.EMail
import net.funpodium.member.domain.data.type.Password
import net.funpodium.member.domain.data.type.Status
import net.funpodium.member.domain.repository.MemberRepository
import net.funpodium.member.external.MemberDataFormatValidator
import net.funpodium.member.external.NewPasswordContentGenerator
import net.funpodium.member.external.PasswordEncoder
import net.funpodium.member.external.SessionHandler
import net.funpodium.member.external.TransmitClient
import org.assertj.core.api.Assertions

class MemberServiceTests {

    private lateinit var username: String
    private lateinit var email: String
    private lateinit var password: String
    private var userId: Long = 0
    private val defaultMember =
        Member(1, "default", "abc@xyz.com", Password(null, "hash"), Status.ACTIVE, "", "", 0, 0, 0)
    private val memberRepository = mockk<MemberRepository>()

    private val passwordEncoder = mockk<PasswordEncoder>()
    private val sessionHandler = mockk<SessionHandler>()
    private val memberDataFormatValidator = mockk<MemberDataFormatValidator>()
    private val transmitClient = mockk<TransmitClient>()
    private val newPasswordContentGenerator = mockk<NewPasswordContentGenerator>()
    private val memberService =
        MemberServiceImpl(
            memberRepository,
            passwordEncoder,
            sessionHandler,
            memberDataFormatValidator,
            transmitClient,
            newPasswordContentGenerator
        )
    private lateinit var loginResult: Operation
    private lateinit var signupResult: Operation
    private lateinit var logoutResult: Operation

    @Given("Pass username and password")
    fun pass_username_and_password() {
        username = "right"
        password = "right"
    }

    @Given("Pass username and wrong password")
    fun pass_username_and_wrong_password() {
        username = "right"
        password = "wrong"
    }

    @Given("Pass email and password")
    fun pass_email_and_password() {
        email = "abc@xyz.com"
        password = "right"
    }

    @Given("Pass email and wrong password")
    fun pass_email_and_wrong_password() {
        email = "abc@xyz.com"
        password = "wrong"
    }

    @When("Login by username")
    fun login_by_username() {
        every { memberRepository.findPasswordByUsername(username) } returns Password(null, "right-hash")
        every { passwordEncoder.hash("right") } returns "right-hash"
        every { passwordEncoder.hash("wrong") } returns "wrong-hash"
        loginResult = memberService.login(username, password)
    }

    @Given("Pass email")
    fun pass_email() {
        email = "abc@xyz.com"
    }

    @Given("Pass existed email")
    fun pass_existed_email() {
        email = "existed@xyz.com"
    }

    @Given("Pass user id")
    fun pass_user_id() {
        userId = 1
    }

    @When("Login by email")
    fun login_by_email() {
        every { memberRepository.findPasswordByEmail(email) } returns Password(null, "right-hash")
        every { passwordEncoder.hash("right") } returns "right-hash"
        every { passwordEncoder.hash("wrong") } returns "wrong-hash"
        loginResult = memberService.login(EMail(email), password)
    }

    @When("Signup")
    fun signup() {
        every { memberRepository.findByEmail("abc@xyz.com") } returns null
        every { memberRepository.findByEmail("existed@xyz.com") } returns defaultMember
        every { newPasswordContentGenerator.signupEmail() } returns "Hi, your password is xxx, please reset after you first login."
        every {
            transmitClient.sendEmail(
                email,
                "Hi, your password is xxx, please reset after you first login."
            )
        } returns "0042cd2f-05c4-4941-9af9-e3d2522aa905"

        signupResult = memberService.signup(email)
    }

    @When("Logout")
    fun logout() {
        every { sessionHandler.invalid("user:1") } returns Unit

        logoutResult = memberService.logout(userId)
    }

    @Then("Login response should be success")
    fun login_response_should_be_success() {
        Assertions.assertThat(loginResult).isEqualTo(Operation.LOGIN_SUCCESS)
    }

    @Then("Login response should be fail")
    fun login_response_should_be_fail() {
        Assertions.assertThat(loginResult).isEqualTo(Operation.LOGIN_FAIL)
    }

    @Then("Signup response should be success")
    fun signup_response_should_be_success() {
        Assertions.assertThat(signupResult).isEqualTo(Operation.SIGNUP_SUCCESS)
    }

    @Then("Signup response should be fail")
    fun signup_response_should_be_fail() {
        Assertions.assertThat(signupResult).isEqualTo(Operation.SIGNUP_FAIL)
    }

    @Then("Logout response should be success")
    fun logout_response_should_be_success() {
        Assertions.assertThat(logoutResult).isEqualTo(Operation.LOGOUT_SUCCESS)
    }
}