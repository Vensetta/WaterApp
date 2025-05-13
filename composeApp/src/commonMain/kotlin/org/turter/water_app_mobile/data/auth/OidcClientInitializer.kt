package org.turter.water_app_mobile.data.auth

import org.publicvalue.multiplatform.oidc.OpenIdConnectClient
import org.publicvalue.multiplatform.oidc.types.CodeChallengeMethod

class OidcClientInitializer {
    private val BASE_AUTH_URL = "http://192.168.30.110:8080/realms/WaterApp"
    
    val client = OpenIdConnectClient {
        endpoints {
            tokenEndpoint = "$BASE_AUTH_URL/protocol/openid-connect/token"
            authorizationEndpoint = "$BASE_AUTH_URL/protocol/openid-connect/auth"
            userInfoEndpoint = "$BASE_AUTH_URL/protocol/openid-connect/userinfo"
            endSessionEndpoint = "$BASE_AUTH_URL/protocol/openid-connect/logout"
        }
        clientId = "water-app-mobile"
//        clientSecret = "Bw2HZbLYeSChGzJ3SsVR7MpErqHtuYD7"
        scope = "openid profile"
        codeChallengeMethod = CodeChallengeMethod.S256
        redirectUri = "water.app.mobile://callback"
    }
}