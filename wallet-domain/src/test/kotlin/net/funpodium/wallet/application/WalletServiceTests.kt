package net.funpodium.wallet.application

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.purefunc.common.domain.data.type.Page
import net.funpodium.wallet.application.impl.WalletServiceImpl
import net.funpodium.wallet.domain.data.entity.Wallet
import net.funpodium.wallet.domain.data.entity.WalletTxRecord
import net.funpodium.wallet.domain.data.type.Currency
import net.funpodium.wallet.domain.data.type.Type
import net.funpodium.wallet.domain.exception.BalanceNotEnoughException
import net.funpodium.wallet.domain.exception.InvalidDepositAmountException
import net.funpodium.wallet.domain.exception.InvalidTransferAmountException
import net.funpodium.wallet.domain.exception.WalletNotFoundException
import net.funpodium.wallet.domain.repository.WalletRepository
import net.funpodium.wallet.domain.repository.WalletTxRecordRepository
import net.funpodium.wallet.external.WalletValidator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class WalletServiceTests {

    private val walletRepository = mockk<WalletRepository>()
    private val walletTxRecordRepository = mockk<WalletTxRecordRepository>()
    private val walletValidator = mockk<WalletValidator>()
    private val walletService = WalletServiceImpl(walletRepository, walletTxRecordRepository, walletValidator)
    private val defaultRecords = WalletTxRecord(
        1,
        1,
        1,
        Currency.CNY,
        Type.DEPOSIT,
        "",
        BigDecimal.ZERO,
        BigDecimal.ONE,
        BigDecimal.ONE,
        0,
    )

    @Test
    internal fun `get balance success`() {
        every { walletRepository.findBalanceByMemberIdAndCurrency(1, Currency.CNY) } returns BigDecimal.ONE

        Assertions.assertThat(walletService.getBalance(1, Currency.CNY)).isEqualTo(BigDecimal.ONE)
    }

    @Test
    internal fun `get balance fail because wallet not found`() {
        every { walletRepository.findBalanceByMemberIdAndCurrency(1, Currency.CNY) } returns null

        Assertions.assertThatThrownBy { walletService.getBalance(1, Currency.CNY) }.isInstanceOf(WalletNotFoundException::class.java)
    }

    @Test
    internal fun `deposit success`() {
        every { walletRepository.findBalanceByMemberIdAndCurrency(1, Currency.CNY) } returns BigDecimal("2.0")
        every { walletRepository.findByMemberIdAndCurrency(1, Currency.CNY) } returns Wallet(
            1,
            1,
            Currency.CNY,
            BigDecimal("2.0"),
        )
        every { walletValidator.isInvalidDepositAmount(BigDecimal("998.0"), Currency.CNY) } returns false
        every { walletRepository.addBalanceById(BigDecimal("998.0"), 1) } returns BigDecimal("1000.0")
        every { walletTxRecordRepository.save(any()) } returns WalletTxRecord(
            1,
            1,
            1,
            Currency.CNY,
            Type.DEPOSIT,
            "",
            BigDecimal("2.0"),
            BigDecimal("998.0"),
            BigDecimal("1000.0"),
            0,
        )

        val currentBalance = walletService.getBalance(1, Currency.CNY)
        val afterDeposit = walletService.deposit(1, Currency.CNY, BigDecimal("998.0"))

        verify(exactly = 1) { walletTxRecordRepository.save(any()) }
        Assertions.assertThat(afterDeposit.compareTo(currentBalance.add(BigDecimal("998.0")))).isEqualTo(0)
    }

    @Test
    internal fun `deposit fail because wallet not found`() {
        every { walletRepository.findByMemberIdAndCurrency(1, Currency.CNY) } returns null
        
        Assertions.assertThatThrownBy { walletService.deposit(1, Currency.CNY, BigDecimal("998.0")) }.isInstanceOf(WalletNotFoundException::class.java)
    }

    @Test
    internal fun `deposit fail because amount invalid`() {
        every { walletRepository.findByMemberIdAndCurrency(1, Currency.CNY) } returns Wallet(
            1,
            1,
            Currency.CNY,
            BigDecimal.ONE,
        )
        every { walletValidator.isInvalidDepositAmount(BigDecimal("-1.0"), Currency.CNY) } returns true

        Assertions.assertThatThrownBy { walletService.deposit(1, Currency.CNY, BigDecimal("-1.0")) }
            .isInstanceOf(InvalidDepositAmountException::class.java)

        every { walletRepository.findByMemberIdAndCurrency(1, Currency.VND) } returns Wallet(
            1,
            1,
            Currency.VND,
            BigDecimal.ONE,
        )
        every { walletValidator.isInvalidDepositAmount(BigDecimal("1000000000"), Currency.VND) } returns true

        Assertions.assertThatThrownBy { walletService.deposit(1, Currency.VND, BigDecimal("1000000000")) }
            .isInstanceOf(InvalidDepositAmountException::class.java)
    }

    @Test
    internal fun `withdraw success`() {
        every { walletRepository.findBalanceByMemberIdAndCurrency(1, Currency.CNY) } returns BigDecimal("2.0")
        every { walletRepository.findByMemberIdAndCurrency(1, Currency.CNY) } returns Wallet(
            1,
            1,
            Currency.CNY,
            BigDecimal("2.0"),
        )
        every { walletRepository.minusBalanceById(BigDecimal("1.5"), 1) } returns BigDecimal("0.5")
        every { walletTxRecordRepository.save(any()) } returns WalletTxRecord(
            1,
            1,
            1,
            Currency.CNY,
            Type.WITHDRAW,
            "",
            BigDecimal("1.5"),
            BigDecimal("2.0"),
            BigDecimal("0.5"),
            0,
        )

        val currentBalance = walletService.getBalance(1, Currency.CNY)
        val afterWithdraw = walletService.withdraw(1, Currency.CNY, BigDecimal("1.5"))

        verify(exactly = 1) { walletTxRecordRepository.save(any()) }
        Assertions.assertThat(afterWithdraw.compareTo(currentBalance.minus(BigDecimal("1.5")))).isEqualTo(0)
    }

    @Test
    internal fun `withdraw fail because wallet not found`() {
        every { walletRepository.findByMemberIdAndCurrency(1, Currency.CNY) } returns null

        Assertions.assertThatThrownBy { walletService.withdraw(1, Currency.CNY, BigDecimal("998.0")) }
            .isInstanceOf(WalletNotFoundException::class.java)
    }

    @Test
    internal fun `withdraw fail because balance not enough`() {
        every { walletRepository.findByMemberIdAndCurrency(1, Currency.CNY) } returns Wallet(
            1,
            1,
            Currency.CNY,
            BigDecimal.ONE,
        )

        Assertions.assertThatThrownBy { walletService.withdraw(1, Currency.CNY, BigDecimal("998.0")) }
            .isInstanceOf(BalanceNotEnoughException::class.java)
    }

    @Test
    internal fun `transfer success`() {
        every { walletRepository.findBalanceByMemberIdAndCurrency(1, Currency.CNY) } returns BigDecimal.ONE
        every { walletRepository.findByMemberIdAndCurrency(1, Currency.CNY) } returns Wallet(
            1,
            1,
            Currency.CNY,
            BigDecimal.ONE,
        )
        every { walletRepository.minusBalanceById(BigDecimal.ONE, 1) } returns BigDecimal.ZERO
        every { walletRepository.findBalanceByMemberIdAndCurrency(2, Currency.CNY) } returns BigDecimal.ONE
        every { walletValidator.isInvalidDepositAmount(BigDecimal.ONE, Currency.CNY) } returns false
        every { walletRepository.addBalanceById(BigDecimal.ONE, 2) } returns BigDecimal("2.0")
        every { walletTxRecordRepository.save(any()) } returns WalletTxRecord(
            1,
            1,
            1,
            Currency.CNY,
            Type.TRANSFER_IN,
            "",
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            0,
        )

        val currentBalance1 = walletService.getBalance(1, Currency.CNY)
        val currentBalance2 = walletService.getBalance(2, Currency.CNY)

        val transferResult = walletService.transfer(1, 2, Currency.CNY, BigDecimal.ONE)

        Assertions.assertThat(transferResult.currency).isEqualTo(Currency.CNY)
        Assertions.assertThat(transferResult.fromMemberAfterBalance.compareTo(currentBalance1.minus(BigDecimal.ONE)))
            .isEqualTo(0)
        Assertions.assertThat(transferResult.toMemberAfterBalance.compareTo(currentBalance2.plus(BigDecimal.ONE)))
            .isEqualTo(0)
    }

    @Test
    internal fun `transfer fail because wallet not found`() {
        every { walletRepository.findByMemberIdAndCurrency(1, Currency.CNY) } returns null

        Assertions.assertThatThrownBy { walletService.transfer(1, 2, Currency.CNY, BigDecimal.TEN) }.isInstanceOf(WalletNotFoundException::class.java)
    }

    @Test
    internal fun `transfer fail because balance not enough`() {
        every { walletRepository.findByMemberIdAndCurrency(1, Currency.CNY) } returns Wallet(
            1,
            1,
            Currency.CNY,
            BigDecimal.ONE,
        )

        Assertions.assertThatThrownBy { walletService.transfer(1, 2, Currency.CNY, BigDecimal.TEN) }
            .isInstanceOf(BalanceNotEnoughException::class.java)
    }

    @Test
    internal fun `transfer fail because amount invalid`() {
        every { walletRepository.findByMemberIdAndCurrency(1, Currency.CNY) } returns Wallet(
            1,
            1,
            Currency.CNY,
            BigDecimal.ONE,
        )
        every { walletValidator.isInvalidTransferAmount(BigDecimal.ONE, Currency.CNY) } returns true

        Assertions.assertThatThrownBy { walletService.transfer(1, 2, Currency.CNY, BigDecimal.TEN) }
            .isInstanceOf(InvalidTransferAmountException::class.java)
    }

    @Test
    internal fun `enter back office tx records page`() {
        val records = listOf(defaultRecords, defaultRecords.copy(id = 2), defaultRecords.copy(id = 3))
        every { walletTxRecordRepository.findAllRecordsOrderByIdDesc(Page(0, 10)) } returns records

        Assertions.assertThat(walletService.queryAllTxRecord(Page(0, 10)).isNotEmpty()).isTrue
    }

    @Test
    internal fun `query tx records in back office by member id`() {
        val records = listOf(defaultRecords, defaultRecords.copy(id = 2), defaultRecords.copy(id = 3))
        every { walletTxRecordRepository.findByMemberIdOrderByIdDesc(1, Page(0, 10)) } returns records

        Assertions.assertThat(walletService.queryTxRecordByMemberId(1, Page(0, 10)).isNotEmpty()).isTrue
    }
}