package net.purefunc.oauth.application

import net.purefunc.oauth.domain.data.entity.Member

interface OAuthService {

    fun fetchMember(code: String): Member?
}