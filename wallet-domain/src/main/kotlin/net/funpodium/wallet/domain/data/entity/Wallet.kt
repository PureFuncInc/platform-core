package net.funpodium.wallet.domain.data.entity

import net.funpodium.wallet.domain.data.type.Currency
import net.funpodium.wallet.domain.data.type.Type
import net.funpodium.wallet.domain.data.vo.TransferResult
import net.funpodium.wallet.domain.exception.BalanceNotEnoughException
import net.funpodium.wallet.domain.exception.InvalidDepositAmountException
import net.funpodium.wallet.domain.exception.InvalidTransferAmountException
import net.funpodium.wallet.domain.exception.WalletNotFoundException
import net.funpodium.wallet.domain.repository.WalletRepository
import net.funpodium.wallet.domain.repository.WalletTxRecordRepository
import net.funpodium.wallet.external.WalletValidator
import java.math.BigDecimal
import java.util.UUID

data class Wallet(

    val id: Long?,

    val memberId: Long,

    val currency: Currency,

    val balance: BigDecimal,
) {

    fun deposit(
        amount: BigDecimal,
        walletRepository: WalletRepository,
        walletTxRecordRepository: WalletTxRecordRepository,
        walletValidator: WalletValidator,
    ) = apply {
        if (walletValidator.isInvalidDepositAmount(amount, currency)) throw InvalidDepositAmountException("$amount $currency")
    }.run {
        val afterBalance = walletRepository.addBalanceById(amount, id!!)
        walletTxRecordRepository.save(
            WalletTxRecord(
                null,
                id,
                memberId,
                currency,
                Type.DEPOSIT,
                "",
                amount,
                balance,
                afterBalance,
                System.currentTimeMillis()
            )
        )
        afterBalance
    }

    fun withdraw(
        amount: BigDecimal,
        walletRepository: WalletRepository,
        walletTxRecordRepository: WalletTxRecordRepository,
    ) = apply {
        if (balance.minus(amount).compareTo(BigDecimal.ZERO) == -1) throw BalanceNotEnoughException("$memberId $balance $amount")
    }.run {
        val afterBalance = walletRepository.minusBalanceById(amount, id!!)
        walletTxRecordRepository.save(
            WalletTxRecord(
                null,
                id,
                memberId,
                currency,
                Type.WITHDRAW,
                "",
                amount,
                balance,
                afterBalance,
                System.currentTimeMillis()
            )
        )
        afterBalance
    }

    fun transfer(
        amount: BigDecimal,
        toMemberId: Long,
        walletRepository: WalletRepository,
        walletTxRecordRepository: WalletTxRecordRepository,
        walletValidator: WalletValidator,
    ) = apply {
        if (balance.minus(amount).compareTo(BigDecimal.ZERO) == -1) throw BalanceNotEnoughException("$memberId $balance $amount")
        if (walletValidator.isInvalidTransferAmount(amount, currency)) throw InvalidTransferAmountException("$amount $currency")
    }.let {
        TransferResult(UUID.randomUUID().toString(), id!!, toMemberId, Currency.CNY, BigDecimal.ZERO, BigDecimal.ZERO)
    }.run {
        val afterBalance = walletRepository.minusBalanceById(amount, id!!)
        walletTxRecordRepository.save(
            WalletTxRecord(
                null,
                id,
                memberId,
                currency,
                Type.TRANSFER_OUT,
                this.uuid,
                amount,
                balance,
                afterBalance,
                System.currentTimeMillis()
            )
        )
        this.copy(fromMemberAfterBalance = afterBalance)
    }.run {
        val before = walletRepository.findBalanceByMemberIdAndCurrency(toMemberId, currency) ?: throw WalletNotFoundException("$toMemberId $currency")
        val afterBalance = walletRepository.addBalanceById(amount, toMemberId)
        walletTxRecordRepository.save(
            WalletTxRecord(
                null,
                toMemberId,
                memberId,
                currency,
                Type.TRANSFER_IN,
                this.uuid,
                amount,
                before,
                afterBalance,
                System.currentTimeMillis()
            )
        )
        this.copy(toMemberAfterBalance = afterBalance)
    }
}
