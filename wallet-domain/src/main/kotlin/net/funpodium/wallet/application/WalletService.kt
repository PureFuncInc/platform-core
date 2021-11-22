package net.funpodium.wallet.application

import net.purefunc.core.domain.data.type.Page
import net.funpodium.wallet.domain.data.entity.WalletTxRecord
import net.funpodium.wallet.domain.data.type.Currency
import net.funpodium.wallet.domain.data.vo.TransferResult
import java.math.BigDecimal

interface WalletService {

    // product

    fun getBalance(memberId: Long, currency: Currency): BigDecimal

    fun deposit(memberId: Long, currency: Currency, amount: BigDecimal): BigDecimal

    fun withdraw(memberId: Long, currency: Currency, amount: BigDecimal): BigDecimal

    fun transfer(fromMemberId: Long, toMemberId: Long, currency: Currency, amount: BigDecimal): TransferResult

    // back office

    fun queryAllTxRecord(page: Page): List<WalletTxRecord>

    fun queryTxRecordByMemberId(memberId: Long, page: Page): List<WalletTxRecord>
}
