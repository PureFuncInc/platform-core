package net.purefunc.member.external.impl

import arrow.core.Either.Companion.catch
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import net.purefunc.core.ext.Slf4j
import net.purefunc.member.MemberFunc
import net.purefunc.member.external.OAuthClient

@Slf4j
class GoogleOAuthClient(
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUri: String,
) : OAuthClient, MemberFunc() {

    override suspend fun fetch(accessToken: String, jwtTtlSeconds: Long, role: String) =
        catch {
            GoogleAuthorizationCodeTokenRequest(
                NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                clientId,
                clientSecret,
                accessToken,
                redirectUri,
            ).execute().parseIdToken().payload
                .let {
                    initMember(
                        name = it["name"].toString(),
                        ttlSeconds = jwtTtlSeconds,
                        role = role,
                        email = it["email"].toString(),
                    )
                }
        }
}