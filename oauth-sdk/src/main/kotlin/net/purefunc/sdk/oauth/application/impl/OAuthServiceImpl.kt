package net.purefunc.sdk.oauth.application.impl

import net.purefunc.oauth.application.OAuthService
import net.purefunc.oauth.domain.data.entity.Member

class OAuthServiceImpl: OAuthService {

    override fun fetchMember(code: String): Member? {
        TODO("Not yet implemented")
    }
}