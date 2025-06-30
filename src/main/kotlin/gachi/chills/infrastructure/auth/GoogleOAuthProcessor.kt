package gachi.chills.infrastructure.auth

import gachi.chills.domain.auth.service.adapter.OAuthProcessor
import gachi.chills.domain.auth.service.adapter.OAuthType
import gachi.chills.domain.auth.service.adapter.OAuthResult
import org.springframework.stereotype.Component

@Component
internal class GoogleOAuthProcessor : OAuthProcessor {
    override fun supports(identifier: String): Boolean = identifier == OAuthType.GOOGLE.identifier

    override fun process(code: String): OAuthResult {
        TODO("Not yet implemented")
    }
}
