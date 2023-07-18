package io.rouk.broker.social.spotify

import com.fasterxml.jackson.databind.JsonNode
import org.keycloak.broker.oidc.AbstractOAuth2IdentityProvider
import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig
import org.keycloak.broker.oidc.mappers.AbstractJsonUserAttributeMapper
import org.keycloak.broker.provider.BrokeredIdentityContext
import org.keycloak.broker.provider.IdentityBrokerException
import org.keycloak.broker.provider.util.SimpleHttp
import org.keycloak.broker.social.SocialIdentityProvider
import org.keycloak.events.EventBuilder
import org.keycloak.models.KeycloakSession
import java.io.IOException

private object OAuth2 {
    const val AUTH_URL = "https://accounts.spotify.com/authorize"
    const val TOKEN_URL = "https://accounts.spotify.com/api/token"
    const val PROFILE_URL = "https://api.spotify.com/v1/me"
    const val DEFAULT_SCOPE = "user-read-email"
}

class SpotifyIdentityProvider(session: KeycloakSession, config: OAuth2IdentityProviderConfig):
    AbstractOAuth2IdentityProvider<OAuth2IdentityProviderConfig>(session, config),
    SocialIdentityProvider<OAuth2IdentityProviderConfig> {

    init {
        config.authorizationUrl = OAuth2.AUTH_URL
        config.tokenUrl = OAuth2.TOKEN_URL
        config.userInfoUrl = OAuth2.PROFILE_URL
    }

    override fun supportsExternalExchange() = true
    override fun getProfileEndpointForValidation(event: EventBuilder?) = OAuth2.PROFILE_URL
    override fun getDefaultScopes() = OAuth2.DEFAULT_SCOPE

    override fun doGetFederatedIdentity(accessToken: String): BrokeredIdentityContext {
        try {
            val identity = extractIdentityFromProfile(null, fetchUserInfo(accessToken))

            if (identity.username == null) {
                identity.username = identity.email ?: identity.id
            }

            return identity
        } catch (e: Exception) {
            throw IdentityBrokerException("Failed to extract identity from Spotify. Exception: ", e)
        }
    }

    override fun extractIdentityFromProfile(event: EventBuilder?, profile: JsonNode): BrokeredIdentityContext {
        val user = BrokeredIdentityContext(getJsonProperty(profile, "id"))

        user.idpConfig = config
        user.idp = this

        getJsonProperty(profile, "email").let {
            user.email = it
        }

        AbstractJsonUserAttributeMapper.storeUserProfileForMapper(user, profile, config.alias)

        return user
    }

    @Throws(IOException::class)
    private fun fetchUserInfo(accessToken: String) =
        SimpleHttp
            .doGet(OAuth2.PROFILE_URL, session)
            .header("Authorization", "Bearer $accessToken")
            .asJson()
}
