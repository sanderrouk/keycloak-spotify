package io.rouk.broker.social.spotify

import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig
import org.keycloak.broker.provider.AbstractIdentityProviderFactory
import org.keycloak.broker.social.SocialIdentityProviderFactory
import org.keycloak.models.IdentityProviderModel
import org.keycloak.models.KeycloakSession

private object Spotify {
    const val ID = "spotify"
    const val NAME = "Spotify"
}

class SpotifyIdentityProviderFactory : AbstractIdentityProviderFactory<SpotifyIdentityProvider>(), SocialIdentityProviderFactory<SpotifyIdentityProvider> {

    override fun getId() = Spotify.ID
    override fun getName() = Spotify.NAME

    override fun create(session: KeycloakSession, model: IdentityProviderModel): SpotifyIdentityProvider {
        return SpotifyIdentityProvider(session, OAuth2IdentityProviderConfig(model))
    }

    override fun createConfig(): IdentityProviderModel {
        return OAuth2IdentityProviderConfig()
    }
}
