package net.funpodium.wallet.domain.data.entity

import net.funpodium.wallet.domain.data.type.Currency
import net.funpodium.wallet.domain.data.type.Type
import net.funpodium.wallet.domain.repository.WalletRepository
import java.math.BigDecimal

data class WalletTxRecord(

    val id: Long?,

    val walletId: Long,

    val memberId: Long,

    val currency: Currency,

    val type: Type,

    val transferUuid: String,

    val amount: BigDecimal,

    val before: BigDecimal,

    val after: BigDecimal,

    val createDate: Long,
)
