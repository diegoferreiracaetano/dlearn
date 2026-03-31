package com.diegoferreiracaetano.dlearn

object Constants {
    const val CHALLENGE_TOKEN_PREFIX = "challenge-token-"
    const val VALIDATED_TOKEN_PREFIX = "validated-token-"
    const val DEFAULT_MOCK_OTP = "123456"
    const val DEBUG_OTP = "000000"
    const val DEFAULT_USER_ID = "system_user"
    const val OTP_KEY = "otp"
}

object AppConstants {
    const val AVATAR_PLACEHOLDER = "https://avatars.githubusercontent.com/u/1023?v=4"
    const val X_COUNTRY = "X-Country"
    const val X_NOTIFICATIONS_ENABLED = "X-Notifications-Enabled"
}


object TokenConstants {
    const val CLAIM_USER_ID = "user_id"
    const val CLAIM_EMAIL = "email"
}

object MetadataKeys {
    const val EXTERNAL_ID = "external_id"
    const val AUTH_TYPE = "auth_type"
    const val ACCESS_TOKEN = "access_token"
    const val ID_TOKEN = "id_token"
}

object SocialAuthConstants {
    const val CLAIM_EMAIL = "email"
    const val CLAIM_NAME = "name"
    const val CLAIM_PICTURE = "picture"
    const val CLAIM_GIVEN_NAME = "given_name"
    const val CLAIM_FAMILY_NAME = "family_name"
    
    const val DEFAULT_NAME_SUFFIX = " User"
    const val JWT_SEPARATOR = "."
}

object TmdbConstants {
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
    const val IMAGE_W500 = "w500"
    const val IMAGE_W185 = "w185"
    const val DEFAULT_REGION = "BR"
    const val SITE_YOUTUBE = "YouTube"
    const val TYPE_TRAILER = "Trailer"
    const val TYPE_TEASER = "Teaser"
    const val TAG_SCREEN_ERROR = "SCREEN_ERROR"
}
