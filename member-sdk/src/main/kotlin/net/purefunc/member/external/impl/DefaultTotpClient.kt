package net.purefunc.member.external.impl

import arrow.core.Either.Companion.catch
import dev.samstevens.totp.code.DefaultCodeGenerator
import dev.samstevens.totp.code.DefaultCodeVerifier
import dev.samstevens.totp.code.HashingAlgorithm
import dev.samstevens.totp.qr.QrData
import dev.samstevens.totp.qr.ZxingPngQrGenerator
import dev.samstevens.totp.time.NtpTimeProvider
import net.purefunc.member.domain.data.entity.Member
import net.purefunc.member.external.TotpClient


class DefaultTotpClient : TotpClient {

    override suspend fun genQrCode(member: Member, issuer: String) =
        catch {
            QrData.Builder()
                .label(member.email)
                .secret(member.secret)
                .issuer(issuer)
                .algorithm(HashingAlgorithm.SHA512)
                .digits(6)
                .period(30)
                .build()
                .let {
                    ZxingPngQrGenerator().generate(it)
                }
        }

    override suspend fun verifyCode(member: Member, code: String) =
        catch {
            DefaultCodeVerifier(
                DefaultCodeGenerator(),
                NtpTimeProvider("pool.ntp.org", 5000)
            ).isValidCode(member.secret, code)
        }
}