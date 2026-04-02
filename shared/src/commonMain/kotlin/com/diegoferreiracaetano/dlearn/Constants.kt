package com.diegoferreiracaetano.dlearn

object Constants {
    const val CHALLENGE_TOKEN_PREFIX = "challenge-token-"
    const val VALIDATED_TOKEN_PREFIX = "validated-token-"
    const val DEFAULT_MOCK_OTP = "123456"
    const val DEBUG_OTP = "000000"
    const val DEFAULT_USER_ID = "system_user"
    const val OTP_KEY = "otp"
    const val OTP_CODE_KEY = "otp_code"
    const val EMPTY_STRING = ""
    const val SPACE = " "
    const val DASH = "-"
    const val UNDERSCORE = "_"
    const val COMMA = ","
    const val SEMICOLON = ";"
}

object AppConstants {
    const val AVATAR_PLACEHOLDER = "https://avatars.githubusercontent.com/u/1023?v=4"
    const val X_COUNTRY = "X-Country"
    const val X_NOTIFICATIONS_ENABLED = "X-Notifications-Enabled"
}

object UIConstants {
    const val DEFAULT_CONTENT_RATING = "L"
    const val RATING_FORMAT = "%.1f"
}

object PreferenceConstants {
    const val PREF_NOTIFICATIONS = "pref_notifications"
    const val PREF_LANGUAGE = "pref_language"
    const val PREF_COUNTRY = "pref_country"
}

object LocaleConstants {
    const val LANG_PT_BR = "pt-BR"
    const val LANG_EN_US = "en-US"
    const val LANG_ES_ES = "es-ES"

    const val COUNTRY_BR = "BR"
    const val COUNTRY_US = "US"
    const val COUNTRY_ES = "ES"

    const val LOCALE_STRINGS_BUNDLE = "strings"
}

object ProfileConstants {
    const val ID_MEMBER = "member"
    const val ID_PASSWORD = "password"
    const val ID_NOTIFICATION = "notification"
    const val ID_LANGUAGE = "language"
    const val ID_COUNTRY = "country"
    const val ID_CLEAR_CACHE = "clear_cache"
    const val ID_LEGAL = "legal"
    const val ID_HELP = "help"
    const val ID_ABOUT = "about"

    const val KEY_FULL_NAME = "full_name"
    const val KEY_EMAIL = "email"
    const val KEY_PHONE = "phone"

    const val ACTION_IMAGE_PICKER = "/v1/profile/image"
}

object TokenConstants {
    const val CLAIM_USER_ID = "user_id"
    const val CLAIM_EMAIL = "email"
    const val AUTH_JWT_NAME = "auth-jwt"
    const val REALM_DLEARN = "dlearn"
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

    const val PARAM_API_KEY = "api_key"
    const val PARAM_LANGUAGE = "language"
    const val PARAM_WITH_GENRES = "with_genres"
    const val PARAM_APPEND_TO_RESPONSE = "append_to_response"
    const val PARAM_QUERY = "query"

    const val APPEND_DETAILS = "credits,videos,watch/providers,external_ids"

    const val TYPE_MOVIE = "movie"
    const val TYPE_TV = "tv"
}

object FrameworkConstants {
    const val SWAGGER_PATH = "swagger"
    const val OPENAPI_PATH = "openapi"
    const val DOCUMENTATION_FILE = "documentation.yaml"
}
