package net.funpodium.wallet.application.impl

import net.funpodium.common.domain.data.type.Page
import net.funpodium.wallet.application.WalletService
import net.funpodium.wallet.domain.data.type.Currency
import net.funpodium.wallet.domain.exception.WalletNotFoundException
import net.funpodium.wallet.domain.repository.WalletRepository
import net.funpodium.wallet.domain.repository.WalletTxRecordRepository
import net.funpodium.wallet.external.WalletValidator
import java.math.BigDecimal

class WalletServiceImpl(
    private val walletRepository: WalletRepository,
    private val walletTxRecordRepository: WalletTxRecordRepository,
    private val walletValidator: WalletValidator,
) : WalletService {

    override fun getBalance(memberId: Long, currency: Currency) =
        walletRepository.findBalanceByMemberIdAndCurrency(memberId, currency) ?: throw WalletNotFoundException("$memberId $currency")

    override fun deposit(memberId: Long, currency: Currency, amount: BigDecimal) =
        walletRepository.findByMemberIdAndCurrency(memberId, currency)
            ?.deposit(amount, walletRepository, walletTxRecordRepository, walletValidator)
            ?: throw WalletNotFoundException("$memberId $currency")

    override fun withdraw(memberId: Long, currency: Currency, amount: BigDecimal) =
        walletRepository.findByMemberIdAndCurrency(memberId, currency)
            ?.withdraw(amount, walletRepository, walletTxRecordRepository)
            ?: throw WalletNotFoundException("$memberId $currency")

    override fun transfer(fromMemberId: Long, toMemberId: Long, currency: Currency, amount: BigDecimal) =
        walletRepository.findByMemberIdAndCurrency(fromMemberId, currency)
            ?.transfer(amount, toMemberId, walletRepository, walletTxRecordRepository, walletValidator)
            ?: throw WalletNotFoundException("$fromMemberId $toMemberId $currency")

    override fun queryAllTxRecord(page: Page) = walletTxRecordRepository.findAllRecordsOrderByIdDesc(page)

    override fun queryTxRecordByMemberId(memberId: Long, page: Page) =
        walletTxRecordRepository.findByMemberIdOrderByIdDesc(memberId, page)
}
