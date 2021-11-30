package net.purefunc.member.data.vo

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.Date

class JwtToken {

    companion object {
        private const val ISSUER = "purefunc.net"
        private const val SECRET =
            "2f58d452-b38a-4bd4-a211-5aa61797af3f329b3279-f4eb-4623-888a-8c68d8bc44bc559964d0-5067-445b-a98e-7898cdfb1bba"

        fun generate(id: String, subject: String, issueAt: Long, expiration: Long) =
            System.currentTimeMillis()
                .run {
                    Jwts.builder()
                        .setId(id)
                        .setIssuer(ISSUER)
                        .setSubject(subject)
                        .setAudience(null)
                        .setIssuedAt(Date(issueAt))
                        .setNotBefore(Date(issueAt))
                        .setExpiration(Date(expiration))
                        .signWith(Keys.hmacShaKeyFor(SECRET.toByteArray()), SignatureAlgorithm.HS512)
                        .compact()
                }

        fun retrieveSubject(code: String) =
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.toByteArray()))
                .build()
                .parseClaimsJws(code)
                .body
                .subject
    }
}