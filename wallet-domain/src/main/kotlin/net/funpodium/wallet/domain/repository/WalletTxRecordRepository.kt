package net.funpodium.wallet.domain.repository

import net.funpodium.wallet.domain.data.entity.WalletTxRecord
import net.purefunc.core.domain.data.type.Page

interface WalletTxRecordRepository {

    fun save(walletRecord: WalletTxRecord): WalletTxRecord

    fun findAllRecordsOrderByIdDesc(page: Page): List<WalletTxRecord>

    fun findByMemberIdOrderByIdDesc(memberId: Long, page: Page): List<WalletTxRecord>
}
