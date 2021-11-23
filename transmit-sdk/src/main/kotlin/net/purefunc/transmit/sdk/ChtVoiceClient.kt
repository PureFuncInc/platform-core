package net.purefunc.transmit.sdk

import net.purefunc.core.domain.data.Result
import net.purefunc.transmit.external.VoiceClient

class ChtVoiceClient(
) : VoiceClient {
    override fun send(phone: String, content: String): Result<String> {
        TODO("Not yet implemented")
    }
}