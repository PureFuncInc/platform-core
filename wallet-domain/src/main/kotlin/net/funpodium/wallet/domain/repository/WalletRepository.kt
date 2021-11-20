package net.funpodium.wallet.domain.repository

import net.funpodium.wallet.domain.data.entity.Wallet
import net.funpodium.wallet.domain.data.type.Currency
import java.math.BigDecimal

interface WalletRepository {

    fun findBalanceByMemberIdAndCurrency(memberId: Long, currency: Currency): BigDecimal?

    fun findByMemberIdAndCurrency(memberId: Long, currency: Currency): Wallet?

    fun addBalanceById(amount: BigDecimal, id: Long): BigDecimal

    fun minusBalanceById(amount: BigDecimal, id: Long): BigDecimal
}
