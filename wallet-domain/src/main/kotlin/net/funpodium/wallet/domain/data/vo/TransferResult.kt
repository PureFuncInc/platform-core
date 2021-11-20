package net.funpodium.wallet.domain.data.vo

import net.funpodium.wallet.domain.data.type.Currency
import java.math.BigDecimal

data class TransferResult(

    val uuid: String,

    val fromMemberId: Long,

    val toMemberId: Long,

    val currency: Currency,

    val fromMemberAfterBalance: BigDecimal,

    val toMemberAfterBalance: BigDecimal,
)
