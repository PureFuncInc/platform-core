package net.purefunc.member.external.impl

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import net.purefunc.member.ext.genMemberBy
import net.purefunc.member.external.OAuthClient

class GoogleOAuthClient(
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUri: String,
) : OAuthClient {

    override fun fetch(accessToken: String, jwtTtlSeconds: Long) =
        GoogleAuthorizationCodeTokenRequest(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            clientId,
            clientSecret,
            accessToken,
            redirectUri,
        ).execute()
            .let {
                genMemberBy(it["name"].toString(), jwtTtlSeconds, it["email"].toString())
            }
}