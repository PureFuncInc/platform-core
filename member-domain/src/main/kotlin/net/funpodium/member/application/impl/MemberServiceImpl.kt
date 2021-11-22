package net.funpodium.member.application.impl

import net.purefunc.core.domain.data.type.BusinessOperation
import net.purefunc.core.domain.data.type.Page
import net.funpodium.member.application.MemberService
import net.funpodium.member.domain.data.type.EMail
import net.funpodium.member.domain.data.type.Status
import net.funpodium.member.domain.exception.InvalidDataFormatException
import net.funpodium.member.domain.exception.UserNotFoundException
import net.funpodium.member.domain.repository.MemberRepository
import net.funpodium.member.external.MemberDataFormatValidator
import net.funpodium.member.external.NewPasswordContentGenerator
import net.funpodium.member.external.PasswordEncoder
import net.funpodium.member.external.SessionHandler
import net.funpodium.member.external.TransmitClient

class MemberServiceImpl(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val sessionHandler: SessionHandler,
    private val memberDataFormatValidator: MemberDataFormatValidator,
    private val transmitClient: TransmitClient,
    private val newPasswordContentGenerator: NewPasswordContentGenerator,
) : MemberService {

//    override fun login(username: String, password: String) =
//        memberRepository.findPasswordByUsername(username)
//            ?.valid(password, passwordEncoder)
//            ?: BusinessOperation.LOGIN_FAIL
//
//    override fun login(email: EMail, password: String) =
//        memberRepository.findPasswordByEmail(email.value)
//            ?.valid(password, passwordEncoder)
//            ?: BusinessOperation.LOGIN_FAIL
//
//    override fun signup(email: String) =
//        memberRepository.findByEmail(email)
//            .let {
//                if (null != it) {
//                    BusinessOperation.SIGNUP_FAIL
//                } else {
//                    transmitClient.sendEmail(email, newPasswordContentGenerator.signupEmail())
//                    BusinessOperation.SIGNUP_SUCCESS
//                }
//            }
//
//    override fun logout(userId: Long) =
//        run {
//            sessionHandler.invalid("user:$userId")
//            BusinessOperation.LOGOUT_SUCCESS
//        }

    override fun forgetPasswordResetByEmail(username: String) =
        let {
            memberRepository.findEmailByUsername(username) ?: throw UserNotFoundException(username)
        }.run {
            transmitClient.sendEmail(this, newPasswordContentGenerator.newEmail(username))
        }

    override fun forgetPasswordResetByPhone(username: String) =
        let {
            memberRepository.findPhoneByUsername(username) ?: throw UserNotFoundException(username)
        }.run {
            transmitClient.sendSms(this, newPasswordContentGenerator.newSms(username))
        }

    override fun modifyPasswordById(oldPassword: String, newPassword: String, id: Long) =
        apply {
            memberRepository.findPasswordById(id)?.valid(oldPassword, passwordEncoder)
                ?: throw UserNotFoundException("$id")
            if (memberDataFormatValidator.isUnvalidPassword(newPassword)) throw InvalidDataFormatException(newPassword)
        }.run {
            memberRepository.updatePasswordById(newPassword, id)
        }

    override fun queryAllOrderByIdDesc(page: Page) = memberRepository.findAllOrderByIdDesc(page)

    override fun queryByUsername(username: String) =
        memberRepository.findByUsername(username) ?: throw UserNotFoundException(username)

    override fun queryByEmail(email: String) = memberRepository.findByEmail(email) ?: throw UserNotFoundException(email)

    override fun queryByPhone(phone: String) = memberRepository.findByPhone(phone) ?: throw UserNotFoundException(phone)

    override fun modifyStatusById(status: Status, id: Long) = memberRepository.updateStatusById(status, id)
}
