package com.diegoferreiracaetano.dlearn

object TokenConstants {
    const val SECRET = "my-secret-key-12345"
    const val ISSUER = "dlearn-api"
    const val AUDIENCE = "dlearn-app"
    const val CLAIM_USER_ID = "user_id"
    const val CLAIM_EMAIL = "email"
}

object MetadataKeys {
    const val TMDB_SESSION_ID = "sessionId"
    const val TMDB_ACCOUNT_ID = "accountId"
    const val EXTERNAL_USERNAME = "external_username"
    const val EXTERNAL_PASSWORD = "external_password"
    const val EXTERNAL_ID = "external_id"
    const val AUTH_TYPE = "auth_type"
    const val AUTH_TYPE_FULL = "full_session"
    const val AUTH_TYPE_GUEST = "guest_session"
    const val USERNAME = "username"
    const val GUEST_PREFIX = "guest_"
    const val EXPIRES_AT = "expires_at"
    const val ACCESS_TOKEN = "access_token"
    const val ID_TOKEN = "id_token"
}

object SocialAuthConstants {
    const val PROVIDER_GOOGLE = "google"
    const val PROVIDER_APPLE = "apple"
    
    const val CLAIM_EMAIL = "email"
    const val CLAIM_NAME = "name"
    const val CLAIM_PICTURE = "picture"
    const val CLAIM_GIVEN_NAME = "given_name"
    const val CLAIM_FAMILY_NAME = "family_name"
    
    const val DEFAULT_NAME_SUFFIX = " User"
    const val JWT_SEPARATOR = "."
}

object AnalyticsEvents {
    const val MOVIE_CLICK = "movie_click"
    const val CATEGORY_CLICK = "category_click"
}
