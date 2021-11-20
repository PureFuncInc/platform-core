package net.funpodium.wallet.external

import net.funpodium.wallet.domain.data.type.Currency
import java.math.BigDecimal

interface WalletValidator {

    fun isInvalidDepositAmount(amount: BigDecimal, currency: Currency): Boolean

    fun isInvalidTransferAmount(amount: BigDecimal, cny: Currency): Boolean
}
