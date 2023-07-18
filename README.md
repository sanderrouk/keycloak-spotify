# keycloak-spotify
A simple identity provider extension for Keycloak to be used with Spotify.

## Installation
Download the latest release and place it in your keycloak `opt/keycloak/providers` directory.

Docker step example:
```Dockerfile
ADD --chown=keycloak:keycloak https://github.com/sanderrouk/keycloak-spotify/releases/download/1.0.0-BETA/io.rouk.keycloak-spotify-1.0.0-BETA.jar /opt/keycloak/providers/io.rouk.keycloak-spotify-1.0.0-BETA.jar
```

## Setup
The setup afterwards is simple.

1. Navigate to your keycloak admin panel and choose your realm.
2. Navigate to identity providers, click spotify.
3. Go to https://developer.spotify.com and create an app or navigate to your app settings.
4. Copy the Client ID and Client Secret from your Spotify app settings and add them into the configuration.
5. You are done, if you need to access the Spotify tokens for some reason then you need to use the `broker` client's `read-token` role for that.

## Extra configuration
This is a very simple IdP and Spotify is not really a proper IdP anyways, however they do have some interesting scopes you may want to add to the token.
By default, only the `user-read-email` scope is added.

*Additional scopes:* https://developer.spotify.com/documentation/web-api/concepts/scopes

## Credits
The original proof of concept was done by [JiriHartikka in 5c6c02f](https://github.com/JiriHartikka/keycloak/commit/5c6c02fc359cfa126c805f56d6d99b436d379bb9) 